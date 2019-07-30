package com.e.envirohabit_k.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserHabitModel() {
    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth

    fun addUserHabit(userHabit : UserHabit) {
        db = FirebaseFirestore.getInstance()
        db.collection("userHabits")
            .add(userHabit)
            .addOnSuccessListener {
                Log.d("/UserHabitModel", "userHabit saved successfully")
            }
            .addOnFailureListener {
                Log.d("/UserHabitModel", "failed to save userHabit")
            }
    }

    fun getAllUserActions(callback: (List<UserHabit>) -> Unit) {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        var userHabits : List<UserHabit>

        db.collection("userHabits")
            .whereEqualTo("userId" ,  uid)
            .get()
            .addOnSuccessListener {
                userHabits = it.documents as List<UserHabit>
                callback(userHabits)
            }
            .addOnFailureListener {
                Log.d("/UserHabitModel", "failed to get userHabit")
            }
    }
}