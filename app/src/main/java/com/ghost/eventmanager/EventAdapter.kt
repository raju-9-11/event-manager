package com.ghost.eventmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class EventAdapter(context: Context, event: List<Eventmodel>) : BaseAdapter() {

    private val mcontext: Context = context
    private val nevent: List<Eventmodel> = event

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mcontext)
        val row = layoutInflater.inflate(R.layout.row, parent, false)
        val eventName = row.findViewById<TextView>(R.id.event_name)
        val eventDescription = row.findViewById<TextView>(R.id.event_description)
        val eventImage = row.findViewById<ImageView>(R.id.event_image)
        eventName.text = nevent[position].EventName
        eventDescription.text = nevent[position].EventDescription

        val random = Random().nextInt(2) + 1
        val drawableResource = when (random) {
            1 -> R.drawable.pubg1
            else -> R.drawable.pubg2
        }
        eventImage.setImageResource(drawableResource)


        return row
    }

    override fun getItem(position: Int): Any {
        return nevent[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return nevent.size
    }

}
