package com.e.envirohabit_k.view

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import com.e.envirohabit_k.R
import com.e.envirohabit_k.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_account_info.*

class AccountInfoActivity : AppCompatActivity() {

    private lateinit var userModel: UserModel
    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        editButton.setOnClickListener {
            startActivity(Intent(this, EditInfoActivity::class.java))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userModel = UserModel()

        userModel.getUserData {
            email = it.email
            emailText.text = email
            usernameText.text = it.username
        }

        toggleButton.setOnCheckedChangeListener { _,isChecked ->
            if (isChecked) {
                onStartAnimation()
            } else {
                val valueAnimator = ValueAnimator.ofFloat(0f)

                valueAnimator.addUpdateListener {
                    // 3
                    val value = it.animatedValue as Float
                    // 4
                    changePWView.translationY = value
                }

                valueAnimator.interpolator = LinearInterpolator()
                valueAnimator.duration = 200
                valueAnimator.start()
            }
        }

        submitButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, oldPW.text.toString())
                .addOnSuccessListener {
                    val user = auth.currentUser

                    user?.updatePassword(newPW.text.toString())
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("AccountInfoActivity", "User password updated.")
                                Toast.makeText(this, "Passord endret", Toast.LENGTH_SHORT).show()
                                oldPW.setText("")
                                newPW.setText("")
                                toggleButton.toggle()
                                newPW.hideKeyboard()
                            }
                        }
                }
                }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun onStartAnimation() {
        val valueAnimator = ValueAnimator.ofFloat(0f, -1100f)

        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            changePWView.translationY = value
        }
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 100
        valueAnimator.start()
    }
}