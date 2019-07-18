package com.e.envirohabit_k.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.envirohabit_k.R
import com.e.envirohabit_k.model.User
import com.e.envirohabit_k.model.UserModel
import kotlinx.android.synthetic.main.activity_edit_info.*

class EditInfoActivity : AppCompatActivity() {

    lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)

        userModel = UserModel()

        userModel.getUserData {
            editEmail.setText(it.email)
            editUsername.setText(it.username)
        }

        submitButton.setOnClickListener {
            saveData()
            startActivity(Intent(this, AccountInfoActivity::class.java))
        }

    }

    private fun saveData() {
        val user = User(editUsername.text.toString(), editEmail.text.toString(), 0)
        userModel.updateUserData(user)
    }
}
