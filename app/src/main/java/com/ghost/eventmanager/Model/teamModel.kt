package com.ghost.eventmanager.Model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QueryDocumentSnapshot

data class teamModel(
    val teamCode: String,
    val teamId: String,
    val teamManager: DocumentReference,
    val teamMembers: MutableList<DocumentReference>,
    val teamName: String
)

fun getTeamdata(data: QueryDocumentSnapshot): teamModel {
    return data.run {
        teamModel(
            teamCode = getString("team_code")!!,
            teamManager = getDocumentReference("team_manager")!!,
            teamId = getString("team_id")!!,
            teamMembers = get("team_members") as MutableList<DocumentReference>,
            teamName = getString("team_name")!!
        )
    }
}

