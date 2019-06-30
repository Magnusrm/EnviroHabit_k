package com.e.envirohabit_k

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            registerUser()
            logIn()
        }
    }

    private fun registerUser() {
        val email = email_input.text.toString()
        val password = password_input.toString()
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

                FirebaseFirestore.getInstance().collection("users").document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        Log.d("Main", "FUNKEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE")

                    }
                    .addOnFailureListener {
                        Log.d("Main", "Funk ikkje")
                    }


            }
            .addOnFailureListener {
                Log.d("Main", it.message.toString())
            }



    }

    private fun logIn() {

    }

    private fun createUserData(uid : String) {

    }
}
