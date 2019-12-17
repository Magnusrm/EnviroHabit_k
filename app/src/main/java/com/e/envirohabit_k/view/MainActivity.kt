package com.e.envirohabit_k.view

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.e.envirohabit_k.R
import com.e.envirohabit_k.model.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.type.Date
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dl : DrawerLayout
    private lateinit var abdt : ActionBarDrawerToggle
    //lateinit var points : TextView
    private lateinit var welcomeMessage : TextView
    private lateinit var userModel : UserModel
    private lateinit var auth : FirebaseAuth
    private lateinit var historyView : ListView
    private lateinit var habitListView : ListView
    private lateinit var userActionModel : UserActionModel


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
            newActionCard.animate().translationY(-800.toPx().toFloat())
        }
        closeNewActionButton.setOnClickListener {
            noteText.hideKeyboard()
            newActionCard.animate().translationY(0f)
        }
        myHabitsButton.setOnClickListener {
            myHabitsCard.animate().translationY(-800.toPx().toFloat())
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
                R.id.logout_item ->
                    FirebaseAuth.getInstance().signOut()
                        .also {

                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                else -> (Toast.makeText(this, "Menyvalg har ingen destinasjon", Toast.LENGTH_SHORT).show())
            }
            true
        }

        //init listview
        historyView = findViewById<ListView>(R.id.action_history)

        //get useractions
        userActionModel = UserActionModel()
        userActionModel.getAllUserActions {
            historyView.adapter = ActionListAdapter(this, R.layout.row_action_history, it)
        }

        habitListView = findViewById<ListView>(R.id.habitsList)

        val habitsList = resources.getStringArray(R.array.habits_array).toList()

        habitListView.adapter = HabitListAdapter(this, R.layout.row_habits, habitsList)

        var isChecked = false
        history_button.setOnClickListener {
            isChecked = !isChecked
            if(isChecked) {
                history_card.animate().translationY(-725.toPx().toFloat())
                history_button.animate().rotation(history_button.rotation-180).start()
            } else {
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

    fun Int.toPx() : Int = (this * Resources.getSystem().displayMetrics.density.toInt())

    private class HabitListAdapter(context : Context, viewId: Int, habitValues : List<String>) : BaseAdapter() {
        private val myContext : Context
        private val myView : Int
        private val habits = habitValues
        private lateinit var activeHabitsList : MutableMap<String, Boolean>
        private var userHabitModel : UserHabitModel

        init {
            myContext = context
            myView = viewId

            activeHabitsList = mutableMapOf<String, Boolean>()
            userHabitModel = UserHabitModel()
            userHabitModel.getActiveUserHabits {userHabitList : List<UserHabit> ->
                habits.forEach habitsList@{ habitString : String ->
                    userHabitList.forEach userHabitList@{ userHabit : UserHabit ->
                        if (habitString == userHabit.habitName) {
                            activeHabitsList[habitString] = true
                            return@userHabitList
                        } else {
                            activeHabitsList[habitString] = false
                        }
                    }
                }
            }
        }

        override fun getCount(): Int {
            return habits.size
        }

        override fun getItem(position: Int): Any {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getView(position: Int, contextView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(myContext)
            val rowMain = layoutInflater.inflate(myView, viewGroup, false)
            val habitText = rowMain.findViewById<TextView>(R.id.habitText)
            val habitSwitch = rowMain.findViewById<Switch>(R.id.habitSwitch)

            habitText.text = habits[position]
            if (activeHabitsList[habits[position]] == true) {
                habitSwitch.toggle()
            }
            return rowMain

        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
    }

    private class ActionListAdapter(context : Context, viewId: Int, list : List<UserAction>) : BaseAdapter() {
        private val myContext : Context
        private val myView : Int
        private val myList = list
        init {
            myContext = context
            myView = viewId
        }

        override fun getCount(): Int {
            return myList.size
        }

        override fun getItem(position: Int): Any {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getView(position: Int, contextView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(myContext)
            val rowMain = layoutInflater.inflate(myView, viewGroup, false)
            val rowTitle = rowMain.findViewById<TextView>(R.id.row_title)
            val rowDate = rowMain.findViewById<TextView>(R.id.row_date)
            val rowNote = rowMain.findViewById<TextView>(R.id.row_note)
            rowTitle.text = myList.get(position).actionName
            rowDate.text = "${myList.get(position).timeRegistered.toDate().day} ${myList.get(position).timeRegistered.toDate().month}"
            rowNote.text = myList.get(position).note
            return rowMain
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
    }
}