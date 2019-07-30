package com.e.envirohabit_k.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class UserActionModel() {
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    fun addUserAction(userAction : UserAction) {
        db = FirebaseFirestore.getInstance()
        db.collection("userActions")
            .add(userAction)
            .addOnSuccessListener {
                Log.d("/UserActionModel", "userAction saved successfully")
            }
            .addOnFailureListener {
                Log.d("/UserActionModel", "failed to save userAction")
            }
    }

    fun getAllUserActions(callback: (List<UserAction>) -> Unit) {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        var userActions : List<UserAction>

        db.collection("userActions")
            .whereEqualTo("userId" ,  uid)
            .get()
            .addOnSuccessListener {
                userActions = it.documents as List<UserAction>
                callback(userActions)
            }
            .addOnFailureListener {
                Log.d("/UserActionModel", "failed to get userActions")
            }
    }
}