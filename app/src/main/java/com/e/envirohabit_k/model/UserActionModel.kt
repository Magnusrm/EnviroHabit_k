package com.e.envirohabit_k.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class UserActionModel() {
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    fun addUserAction (userAction : UserAction, callback : (Boolean) -> Unit) {
        db = FirebaseFirestore.getInstance()
        var actionAdded : Boolean
        db.collection("userActions")
            .add(userAction)
            .addOnSuccessListener {
                Log.d("/UserActionModel", "userAction saved successfully")
                actionAdded = true
                callback(actionAdded)
            }
            .addOnFailureListener {
                Log.d("/UserActionModel", "failed to save userAction")
                actionAdded = false
                callback(actionAdded)
            }
    }

    fun getAllUserActions(callback: (List<UserAction>) -> Unit) {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        var userActions : MutableList<UserAction>

        db.collection("userActions")
            .whereEqualTo("userId" ,  uid)
            .get()
            .addOnSuccessListener {
                userActions = mutableListOf<UserAction>()
                it.documents.forEach {

                    userActions.add(UserAction(it.get("actionName").toString(), it.getTimestamp("timeRegistered") as Timestamp,
                        it.get("note").toString(), it.get("userId").toString()))
                }
                callback(userActions)
            }
            .addOnFailureListener {
                Log.d("/UserActionModel", "failed to get userActions")
            }
    }
}