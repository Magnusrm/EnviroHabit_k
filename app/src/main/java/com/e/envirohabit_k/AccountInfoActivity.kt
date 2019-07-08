package com.e.envirohabit_k

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_account_info.*

class AccountInfoActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var database : DatabaseReference

    private var userListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
            .child("users").child(firebaseAuth.currentUser?.uid.toString())

        editButton.setOnClickListener {
            startActivity(Intent(this, EditInfoActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()


        val userListener = object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val user = dataSnapshot.getValue(User::class.java)

                usernameText.text = user?.username.toString()
                emailText.text = user?.email.toString()
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("Main", "canceled") //To change body of created functions use File | Settings | File Templates.
            }
        }

        database.addListenerForSingleValueEvent(userListener)

        this.userListener = userListener

    }

    public override fun onStop() {
        super.onStop()

        // Remove post value event listener
        userListener?.let {
            database.removeEventListener(it)
        }

    }
}