package com.ghost.eventmanager

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ghost.eventmanager.Model.Eventmodel
import com.ghost.eventmanager.Model.getevent
import com.ghost.eventmanager.Model.toMap
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_event_details.*
import java.util.*

class Eventdetails : AppCompatActivity() {

    lateinit private var eventId: String
    //private var TAG = "Event_details"

    //FireStore Initialization

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val query = db.collection("available_event")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        eventId = intent.extras?.get("Event_Id").toString()

        val random = Random().nextInt(2) + 1
        val drawableResource = when (random) {
            1 -> R.drawable.pubg5
            else -> R.drawable.pubg4
        }
        event_image.setImageResource(drawableResource)

        if (eventId != "") {

            query
                .whereEqualTo("event_id", eventId)
                .get()
                .addOnSuccessListener { documents ->
                    val events: MutableList<Eventmodel> = mutableListOf()
                    val id = mutableListOf<String>()
                    for (document in documents) {
                        events.add(getevent(document))
                        id.add(document.id)
                    }

                    set(events[0], id[0], this)

                }
                .addOnFailureListener { exception -> }
        } else {
            val prev_id = intent.extras?.get("Prev_Id").toString().toLong()
            createevent(this, prev_id)
        }


    }

    //For Update event
    fun set(event: Eventmodel, id: String, context: Context) {
        add.text = "Update"
        delete.visibility = View.VISIBLE
        delete.setOnClickListener {
            val dialog: AlertDialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Deleting Event")
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    db.collection("available_event").document(id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Event Deleted", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error try again!!", Toast.LENGTH_SHORT).show()
                        }

                    val intent = Intent(this, EventListActivity::class.java)
                    startActivity(intent)
                }

            }

            builder.setMessage("The event ${event.EventName} will be deleted")
            builder.setPositiveButton("YES", dialogClickListener)
            builder.setNegativeButton("Cancel", dialogClickListener)
            dialog = builder.create()
            dialog.show()


        }
        val name = findViewById<TextView>(R.id.event_name)
        val description = findViewById<TextView>(R.id.event_description)
        name.setText(event.EventName)
        description.setText(event.EventDescription)
        event_id.setText(event.EventId)
        add.setOnClickListener {
            val event = db.collection("available_event").document(id)
            event.update("event_name", name.text.toString()).addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show()
                }
            event.update("event_description", description.text.toString())
            val intent = Intent(context, EventListActivity::class.java)
            startActivity(intent)
        }


    }

    //For New event
    fun createevent(context: Context, prev_id: Long) {
        event_id.visibility = View.GONE

        add.setOnClickListener {
            val event = Eventmodel(
                EventName = event_name.text.toString(),
                EventDescription = event_description.text.toString(),
                EventId = "${prev_id + 1}"
            )
            query.add(event.toMap()).addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                }
            val intent = Intent(context, EventListActivity::class.java)
            startActivity(intent)
        }

    }
}