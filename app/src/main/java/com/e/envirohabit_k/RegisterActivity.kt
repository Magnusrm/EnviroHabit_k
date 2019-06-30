package com.e.envirohabit_k

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var authInstance: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        register_button.setOnClickListener {
            val username = findViewById<View>(R.id.username_input) as EditText
            val email = findViewById<View>(R.id.email_input) as EditText
            val password = findViewById<View>(R.id.password_input) as EditText


            registerUser(email.text.toString(), password.text.toString())

            saveData(username.text.toString())
        }
    }

    private fun registerUser(email: String, password: String) {
        authInstance = FirebaseAuth.getInstance()

        authInstance.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "successfully created user with uid: $email")

            }
            .addOnFailureListener {
                Log.d("Main", it.message.toString())
            }


    }
        private fun saveData(username: String) {

            val db = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            db.firestoreSettings = settings

            val uid = authInstance.currentUser?.uid.toString()

            val user = hashMapOf(
                "uid" to uid,
                "username" to username,
                "points" to 0
            )

            Log.d("Main", "hehheh")
            Log.d("Main", user.toString())

                db.collection("users").document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        Log.d("Main", "FUNKEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE")

                    }
                    .addOnFailureListener {
                        Log.d("Main", "Funk ikkje")
                    }


        }
}
