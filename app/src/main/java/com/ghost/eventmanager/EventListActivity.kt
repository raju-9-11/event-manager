package com.ghost.eventmanager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ghost.eventmanager.Model.Eventmodel
import com.ghost.eventmanager.Model.getevent
import com.ghost.eventmanager.R.id.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_event_list.*


class EventListActivity : AppCompatActivity() {

    //Initialize firestore instance

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val events: Query = db.collection("available_event")
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    //val TAG = "EventListActivity"
    lateinit var prev_id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)


        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, Login::class.java))
        }


        hide()
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Handler().postDelayed({
                    hide()
                }, 1500)
            }
        }


        Event_list.adapter = EventAdapter(this, listOf(), TYPE.HOME)
//        adapter = EventAdapter(this, listOf(), TYPE.HOME)
        updateList(TYPE.HOME)
        add_event.setOnClickListener {
            val intent = Intent(this, Eventdetails::class.java)
            intent.putExtra("Event_Id", "")
            intent.putExtra("Prev_Id", prev_id)
            startActivity(intent)
        }
        //List Item
        val nav = nav_bar.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> {
                    add_event.visibility = View.VISIBLE
                    title_.text = "Available Events"
                    updateList(TYPE.HOME)
                }

                nav_send -> {
                    Toast.makeText(this, "Select a event to send password", Toast.LENGTH_SHORT)
                        .show()
                    add_event.visibility = View.GONE
                    updateList(TYPE.PASSWORD)
                    title_.text = "Send Password"
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
        nav_bar.selectedItemId = nav_home

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.logout, menu)
        hide()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, Login::class.java))

        return super.onOptionsItemSelected(item)

    }


    private fun hide() {
        window.decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
        )
    }

    //    private suspend fun wait() = run
    private fun updateList(type: TYPE) {
        events.orderBy("event_id").get()
            .addOnSuccessListener { documents ->
                val events: MutableList<Eventmodel> = mutableListOf()
                for (document in documents) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    events.add(getevent(document))
                }
                events.sortBy { it.EventId.toLong() }
                val l = events.size
                if (l > 0) {
                    prev_id = events[l - 1].EventId
                } else {
                    prev_id = "0"
                }
                (Event_list.adapter as EventAdapter).changeData(events, type)
            }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "get failed with ", exception)
                Toast.makeText(this, "Error Loading", Toast.LENGTH_SHORT).show()
            }

    }

}