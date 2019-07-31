package com.e.envirohabit_k.view

import android.content.Context
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_account_info.*
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

        // setting greeting message
        welcomeMessage = findViewById(R.id.welcome_message)
        //points = findViewById(R.id.points_view)
        userModel = UserModel()
        userModel.getUserData {
            welcomeMessage.text = "Velkommen tilbake, ${it.username}"
        }

        // setup new action view
        val actionList = resources.getStringArray(R.array.actions_array)
        val actionPoints = resources.getIntArray(R.array.action_points_array)
        var selectedAction = ""
        var pointReward = 0L
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, actionList)
        actionSelectSpinner.adapter = adapter
        actionSelectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedAction = "nuthin"
                pointReward = 0L
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAction = actionList[position]
                pointReward = actionPoints[position].toLong()
            }
        }

        // setup onclick listeners
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
            addAction(selectedAction, noteText.text.toString(), pointReward)
        }

        // setup nav menu
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

        var isChecked = false
        history_button.setOnClickListener {
            isChecked = !isChecked
            if(isChecked) {
                //onStartAnimation()
                history_card.animate().translationY(-1100f)
                history_button.animate().rotation(history_button.rotation-180).start()
            } else {
                /*
                val valueAnimator = ValueAnimator.ofFloat(0f)

                valueAnimator.addUpdateListener {
                    // 3
                    val value = it.animatedValue as Float
                    // 4
                    history_card.translationY = value
                }

                valueAnimator.interpolator = LinearInterpolator()
                valueAnimator.duration = 200
                valueAnimator.start()
                */
                history_button.animate().rotation(history_button.rotation-180).start()

                history_card.animate().translationY(0f)
            }
        }








    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun addAction(action : String, note : String, points : Long){
        auth = FirebaseAuth.getInstance()
        val userActionModel = UserActionModel()
        val time = Timestamp.now()
        val userAction = UserAction(action, time, note, auth.currentUser?.uid.toString())
        userActionModel.addUserAction(userAction) {
            if (it) {
                userModel.addPoints(points)
                Toast.makeText(this, "Handling registrert", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Handling kunne ikke registreres", Toast.LENGTH_SHORT).show()
            }
        }

        noteText.setText("")
    }

    fun onStartAnimation() {
        val valueAnimator = ValueAnimator.ofFloat(0f, -1100f)

        valueAnimator.addUpdateListener {
            // 3
            val value = it.animatedValue as Float
            // 4
            history_card.translationY = value
        }

        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 100
        valueAnimator.start()
    }
}