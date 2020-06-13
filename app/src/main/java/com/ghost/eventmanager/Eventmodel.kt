package com.ghost.eventmanager

import com.google.firebase.firestore.QueryDocumentSnapshot

data class Eventmodel(
    val EventName: String,
    val EventDescription: String,
    val EventId: String
)

fun getevent(data: QueryDocumentSnapshot): Eventmodel {
    return data.run {
        Eventmodel(
            EventId = getString("event_id") ?: "",
            EventDescription = getString("event_description") ?: "",
            EventName = getString("event_name") ?: ""
        )
    }
}

fun Eventmodel.toMap(): Map<String, String> {
    return mapOf(
        "event_id" to EventId,
        "event_name" to EventName,
        "event_description" to EventDescription
    )
}