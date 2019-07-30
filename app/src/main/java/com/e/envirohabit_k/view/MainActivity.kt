package com.e.envirohabit_k.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.e.envirohabit_k.R
import com.e.envirohabit_k.model.UserAction
import com.e.envirohabit_k.model.UserActionModel
import com.e.envirohabit_k.model.UserModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dl : DrawerLayout
    private lateinit var abdt : ActionBarDrawerToggle
    //lateinit var points : TextView
    private lateinit var welcomeMessage : TextView
    private lateinit var userModel : UserModel
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeMessage = findViewById(R.id.welcome_message)
        //points = findViewById(R.id.points_view)
        userModel = UserModel()
        userModel.getUserData {
            welcomeMessage.text = "Velkommen tilbake, ${it.username}"
        }

        val actionList = resources.getStringArray(R.array.actions_array)
        var selectedAction = ""
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, actionList)
        actionSelectSpinner.adapter = adapter
        actionSelectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedAction = "nuthin"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAction = actionList[position]
            }
        }

        newActionButton.setOnClickListener {
            newActionCard.animate().translationY(-1530f)
        }
        closeNewActionButton.setOnClickListener {
            noteText.hideKeyboard()
            newActionCard.animate().translationY(0f)
        }
        myHabitsButton.setOnClickListener {
            myHabitsCard.animate().translationY(-1530f)
        }
        closeMyHabitsButton.setOnClickListener {
            myHabitsCard.animate().translationY(0f)
        }
        addActionButton.setOnClickListener {
            addAction(selectedAction, noteText.text.toString(), 10)
        }

        dl = findViewById(R.id.dl)
        abdt = ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close)

        dl.addDrawerListener(abdt)
        abdt.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView = findViewById<NavigationView>(R.id.nav_view)

        navView.setNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.ny_miljohandling_item -> (Toast.makeText(this, "Ny MiljøHandling", Toast.LENGTH_SHORT).show())
                R.id.kontooversikt_item -> (startActivity(Intent(this, AccountInfoActivity::class.java)))
                R.id.statistikk_item -> (Toast.makeText(this, "Statistikk", Toast.LENGTH_SHORT).show())
                R.id.instillinger_item -> (startActivity(Intent(this, SettingsActivity::class.java)))
                else -> (Toast.makeText(this, "Menyvalg har ingen destinasjon", Toast.LENGTH_SHORT).show())
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun addAction(action : String, note : String, points : Int) {
        auth = FirebaseAuth.getInstance()
        val userActionModel = UserActionModel()
        val time = Timestamp.now()
        val userAction = UserAction(action, time, points, note, auth.currentUser?.uid.toString())
        userActionModel.addUserAction(userAction)
    }
}