package com.ghost.eventmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_event_details.*
import org.w3c.dom.Text
import java.util.*

class Event_details : AppCompatActivity() {

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
            1 -> R.drawable.pubg3
            else -> R.drawable.pubg4
        }
        event_image.setImageResource(drawableResource)

        if (eventId != "") {

            query
                .whereEqualTo("event_id", eventId)
                .get()
                .addOnSuccessListener { documents ->
                    val events: MutableList<Eventmodel> = mutableListOf()
                    var id = mutableListOf<String>()
                    for (document in documents) {
                        //Log.d(TAG, "${document.id} => ${document.data}")
                        events.add(getevent(document))
                        id.add(document.id)
                    }
                    set(events[0], id[0], this)

                }
                .addOnFailureListener { exception ->
                    //Log.w(TAG, "Error getting documents: ", exception)
                }
        } else {
            createevent(this)
        }


    }

    //For Update event
    fun set(event: Eventmodel, id: String, context: Context) {
        add.text = "Update"
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
    fun createevent(context: Context) {
        event_id.visibility = View.GONE

        add.setOnClickListener {
            val event = Eventmodel(
                EventName = event_name.text.toString(),
                EventDescription = event_description.text.toString(),
                EventId = "2"
            )
            query.add(event.toMap()).addOnSuccessListener {
                Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                }
            val intent = Intent(context, EventListActivity::class.java)
            startActivity(intent)
        }

    }
}