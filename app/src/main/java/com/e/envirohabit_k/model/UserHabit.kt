package com.e.envirohabit_k.model

import com.google.firebase.Timestamp

data class UserHabit(
    val userId : String,
    val habitName : String,
    val timeStarted : Timestamp,
    val timeEnded : Timestamp
)