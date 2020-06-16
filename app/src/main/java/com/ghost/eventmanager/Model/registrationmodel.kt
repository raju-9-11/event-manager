package com.ghost.eventmanager.Model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QueryDocumentSnapshot

data class regmodel(
    val date: Timestamp,
    val event: DocumentReference,
    val slot: Long,
    val team: DocumentReference

)

fun getdata(data: QueryDocumentSnapshot): regmodel {
    return data.run {
        regmodel(
            date = getTimestamp("date")!!,
            event = getDocumentReference("selected_event")!!,
            slot = getLong("selected_slot")!!,
            team = getDocumentReference("team")!!
        )
    }
}

