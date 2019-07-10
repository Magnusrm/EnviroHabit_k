package com.e.envirohabit_k.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.e.envirohabit_k.R
import com.e.envirohabit_k.model.User
import com.e.envirohabit_k.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var userModel : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        db = FirebaseFirestore.getInstance()

        register_button.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = email_input.text.toString()
        val password = password_input.text.toString()
        firebaseAuth = FirebaseAuth.getInstance()


        userModel = UserModel()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "successfully created user with uid: $email")
                val uid = firebaseAuth.currentUser?.uid.toString()

                val user = User(username_input.text.toString(), email, 0)
                userModel.addUserData(user, uid)
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                Log.d("Main", it.message.toString())
            }
    }
}