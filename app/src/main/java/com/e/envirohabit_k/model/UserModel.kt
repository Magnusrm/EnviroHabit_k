package com.e.envirohabit_k.model

import android.util.Log
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.e.envirohabit_k.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_account_info.*

class UserModel{

    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var user : User

     fun getUserData(myCallback : (User) -> Unit) {
        user = User("", "", 0)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        db.collection("users").document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    Log.d("UserModel", "DocumentSnapshot data: ${documentSnapshot.data}")
                    val email = documentSnapshot.getString("email").toString()
                    val username = documentSnapshot.getString("username").toString()
                    val points = documentSnapshot.get("points").toString().toInt()
                    user = User(username, email, points)
                } else {
                    Log.d("UserModel", "No such document")
                }
                myCallback(user)
            }
            .addOnFailureListener { exception ->
                Log.d("UserModel", "get failed with ", exception)
            }
    }

    fun addUserData(user : User, uid : String) {
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        db.collection("users").document(uid)
                .set(user)
                    .addOnSuccessListener {
                        Log.d("/UserModel", "userdata saved successfully")
                    }
                    .addOnFailureListener {
                        Log.d("/UserModel", "failed to save userdata")
                    }
    }

    fun updateUserData(user : User) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        auth.currentUser?.updateEmail(user.email)

        db.collection("users").document(auth.currentUser?.uid.toString())
            .update("email", user.email,
                "username", user.username)
            .addOnSuccessListener {
                Log.d("/UserModel", "userdata updated successfully")
            }
            .addOnFailureListener {
                Log.d("/UserModel", "failed to update userdata")
            }
    }

}