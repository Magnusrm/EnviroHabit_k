package com.e.envirohabit_k.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.e.envirohabit_k.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
        register_button.setOnClickListener {
            registerUser(db)
        }
    }

    private fun registerUser(db : FirebaseFirestore) {
        val email = email_input.text.toString()
        val password = password_input.text.toString()
        val authInstance = FirebaseAuth.getInstance()



        authInstance.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "successfully created user with uid: $email")
                val uid = authInstance.currentUser?.uid.toString()

                val user = hashMapOf(
                    "uid" to uid,
                    "email" to email,
                    "username" to username_input.text.toString(),
                    "points" to 0
                )

                db.collection("users").document(uid)
                    .set(user)
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
