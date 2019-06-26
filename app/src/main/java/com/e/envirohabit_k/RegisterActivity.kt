package com.e.envirohabit_k

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = email_input.text.toString()
        val password = password_input.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "successfully created user with uid: $email")
            }
            .addOnFailureListener {
                Log.d("Main", it.message.toString())
            }

    }
}
