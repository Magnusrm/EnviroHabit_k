package com.e.envirohabit_k.model

import com.google.firebase.Timestamp

data class UserAction (
    val actionName : String,
    val timeRegistered : Timestamp,
    val note: String,
    val userId : String
)