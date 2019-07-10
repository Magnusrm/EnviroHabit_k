package com.e.envirohabit_k

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_account_info.*

class AccountInfoActivity : AppCompatActivity() {

    private lateinit var userKey : String
    private lateinit var userReference : DatabaseReference

    private var userListener : ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        editButton.setOnClickListener {
            startActivity(Intent(this, EditInfoActivity::class.java))
        }

        userKey = FirebaseAuth.getInstance().currentUser?.uid.toString()

        userReference = FirebaseDatabase.getInstance().reference.child("users").child(userKey)


    }

    public override fun onStart() {
        super.onStart()

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)
                Log.d("/AccountInfoActivity", user.toString())

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }


    }
}