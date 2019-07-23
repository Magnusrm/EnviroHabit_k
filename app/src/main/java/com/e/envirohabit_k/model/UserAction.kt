package com.e.envirohabit_k.model

import com.google.firebase.Timestamp

data class UserAction (
    val actionId : Any,
    val actionName : String,
    val timeRegistered : Timestamp
)