package com.ghost.eventmanager

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ghost.eventmanager.Model.getUserdata
import com.ghost.eventmanager.Model.getdata
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_send_password.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SendPassword : AppCompatActivity() {


    private val date1: LocalDate = LocalDate.now()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private var formatted: String = date1.format(formatter)


    private lateinit var eventid: String
    private lateinit var eventname: TextView
    private lateinit var eventdescription: TextView
    private lateinit var regdate: TextView

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_password)
        eventname = findViewById(R.id.event_name)
        eventdescription = findViewById(R.id.event_description)
        regdate = findViewById(R.id.reg_date)
        eventid = intent.extras?.get("Event_Id").toString()
        event_id.text = eventid
        eventname.text = intent.extras?.get("Event_Name").toString()
        eventdescription.text = intent.extras?.get("Event_Description").toString()


        //get user phone numbers
        send.setOnClickListener {
            getUsers()

        }
        regdate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    regdate.text = "${dayOfMonth.toString().padStart(2, '0')}-${month.toString()
                        .padStart(2, '0')}-$year"

                },
                year,
                month,
                day
            )
            dpd.show()
        }

    }


    // Get Phone Numbers of users attending selected event
    private fun getUsers() {
        val date = regdate.text.toString()
        if (date != "") {
            formatted = date
        }

        val teamref: MutableList<DocumentReference> = mutableListOf()
        db.collection("registrations/$formatted/$eventid").get().addOnSuccessListener {
            for (doc in it) {
                teamref.add(getdata(doc).team)
            }
        }
        val uid: MutableList<Long> = mutableListOf()
        db.collection("users").get().addOnSuccessListener {
            for (doc in it) {
                if (getUserdata(doc).team in teamref) {
                    uid.add(getUserdata(doc).phnumber)
                }
            }

        }
    }


}