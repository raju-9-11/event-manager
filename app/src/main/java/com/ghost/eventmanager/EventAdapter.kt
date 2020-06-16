package com.ghost.eventmanager

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ghost.eventmanager.Model.Eventmodel
import java.util.*

enum class TYPE {
    HOME, PASSWORD
}

class EventAdapter(
    private val mcontext: Context,
    var nevent: List<Eventmodel>,
    private var type: TYPE
) : RecyclerView.Adapter<EventAdapter.CustomHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        val layoutInflater = LayoutInflater.from(mcontext)
        val row = layoutInflater.inflate(R.layout.row, parent, false)
        return CustomHolder(row)
    }

    override fun getItemCount(): Int {
        return nevent.size
    }

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        holder.bindData(nevent[position])
    }

    inner class CustomHolder(private val row: View) : RecyclerView.ViewHolder(row) {

        fun bindData(event: Eventmodel) {
            val eventName = row.findViewById<TextView>(R.id.event_name)
            val eventDescription = row.findViewById<TextView>(R.id.event_description)
            val eventImage = row.findViewById<ImageView>(R.id.event_image)
            eventName.text = event.EventName
            eventDescription.text = event.EventDescription

            val random = Random().nextInt(3) + 1
            val drawableResource = when (random) {
                1 -> R.drawable.pubg1
                2 -> R.drawable.pubg2
                else -> R.drawable.pubg3
            }

            eventImage.setImageResource(drawableResource)
            row.setOnClickListener {
                if (type == TYPE.HOME) {
                    val intent = Intent(mcontext, Eventdetails::class.java)
                    intent.putExtra("Event_Id", event.EventId)
                    mcontext.startActivity(intent)
                } else {
                    val intent = Intent(mcontext, SendPassword::class.java)
                    intent.putExtra("Event_Id", event.EventId)
                    intent.putExtra("Event_Name", event.EventName)
                    intent.putExtra("Event_Description", event.EventDescription)
                    mcontext.startActivity(intent)
                }
            }
        }
    }

    fun changeData(newEvent: List<Eventmodel>, type: TYPE) {
        nevent = newEvent
        this.type = type
        notifyDataSetChanged()
    }
}
