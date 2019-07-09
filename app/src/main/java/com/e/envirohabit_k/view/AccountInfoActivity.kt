package com.e.envirohabit_k.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.e.envirohabit_k.R
import com.e.envirohabit_k.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_account_info.*

class AccountInfoActivity : AppCompatActivity() {

    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        editButton.setOnClickListener {
            startActivity(Intent(this, EditInfoActivity::class.java))
        }
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
/*
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
        */

        userModel = UserModel()

        val user = userModel.getUserData(auth.currentUser?.uid.toString())
        Log.d("AccountInfo", user.toString())
        emailText.text = user.email
        usernameText.text = user.username

        /*db.collection("users").document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    Log.d("Main", "DocumentSnapshot data: ${documentSnapshot.data}")
                    val email = documentSnapshot.getString("email")
                    val username = documentSnapshot.getString("username")
                    emailText.text = email
                    usernameText.text = username
                } else {
                    Log.d("Main", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Main", "get failed with ", exception)
            }
            */
    }
}