package com.e.envirohabit_k

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<View>(R.id.login_button)
        loginButton.setOnClickListener(View.OnClickListener {
            login()
        })

        val registerButton = findViewById<View>(R.id.register_button)
        registerButton.setOnClickListener(View.OnClickListener {
            regIntent()
        })



    }

    private fun login() {
        val emailText = input_email.text.toString()
        var passordText = input_password.text.toString()

        if (!emailText.isEmpty() && !passordText.isEmpty()) {

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(emailText,passordText)
                .addOnCompleteListener(this, OnCompleteListener {task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("LoginActivity", task.exception.toString())

                    }
                })
        }
    }

    private fun regIntent() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}
