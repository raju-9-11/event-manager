package com.ghost.eventmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_event_list.*


class EventListActivity : AppCompatActivity() {

    //Initialize firestore instance

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val events: Query = db.collection("available_event")
    //val TAG = "EventListActivity"


    lateinit var a: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        //Read Data From FireStore

        events.get()
            .addOnSuccessListener { documents ->
                val events: MutableList<Eventmodel> = mutableListOf()
                for (document in documents) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    events.add(getevent(document))
                }
                Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show()
                Event_list.adapter = EventAdapter(this, events)

                //List Item

                Event_list.setOnItemClickListener { parent, view, position, id ->
                    Toast.makeText(this, "${events[id.toInt()].EventName} ", Toast.LENGTH_LONG)
                        .show()
                    openEvent(events[id.toInt()].EventId)
                }


                //Floating Action Button

                add_event.setOnClickListener {
                    val intent = Intent(this, Event_details::class.java)
                    intent.putExtra("Event_Id", "")
                    startActivity(intent)
                }

            }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "get failed with ", exception)
                Toast.makeText(this, "Error Loading", Toast.LENGTH_SHORT).show()
            }


    }

    private fun openEvent(id: String) {
        val intent = Intent(this, Event_details::class.java)
        intent.putExtra("Event_Id", id)
        startActivity(intent)
    }


}