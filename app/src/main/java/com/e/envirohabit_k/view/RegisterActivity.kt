package com.e.envirohabit_k.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.e.envirohabit_k.R
import com.e.envirohabit_k.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        database = FirebaseDatabase.getInstance().reference

        register_button.setOnClickListener {
            registerUser(database)
        }
    }

    private fun registerUser(db : DatabaseReference) {
        val email = email_input.text.toString()
        val password = password_input.text.toString()
        firebaseAuth = FirebaseAuth.getInstance()



        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "successfully created user with uid: $email")
                val uid = firebaseAuth.currentUser?.uid.toString()

                val user = User(username_input.text.toString(), email, 0)

                database.child("users").child(uid).setValue(user)
                    .addOnSuccessListener {
                        Log.d("Main", "userdata saved successfully")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Log.d("Main", "failed to save userdata")
                    }
            }
            .addOnFailureListener {
                Log.d("Main", it.message.toString())
            }
    }
}