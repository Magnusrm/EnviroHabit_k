package com.e.envirohabit_k.model

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_account_info.*

class UserModel {
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var user : User

     fun getUserData(uid : String) : User {
        user = User("", "", 0)
        db = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    Log.d("UserModel", "DocumentSnapshot data: ${documentSnapshot.data}")
                    val email = documentSnapshot.getString("email").toString()
                    val username = documentSnapshot.getString("username").toString()
                    val points = documentSnapshot.get("points").toString().toInt()
                    user = User(username, email, points)

                    Log.d("UserModel", "User object data: $user")
                } else {
                    Log.d("UserModel", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("UserModel", "get failed with ", exception)
            }
            .isComplete.also { return user }
        Log.d("UserModel", "UserData before return: $user")

    }


}