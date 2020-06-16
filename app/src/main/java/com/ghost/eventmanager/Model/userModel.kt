package com.ghost.eventmanager.Model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QueryDocumentSnapshot

data class userModel(
    val phnumber: Long,
    val team: DocumentReference,
    val uid: String,
    val userName: String
)

fun getUserdata(data: QueryDocumentSnapshot): userModel {
    return data.run {
        userModel(
            phnumber = getLong("phone_number")!!,
            team = getDocumentReference("team")!!,
            uid = getString("user_id")!!,
            userName = getString("user_name")!!
        )
    }
}
