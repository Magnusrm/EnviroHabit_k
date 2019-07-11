package com.e.envirohabit_k.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.e.envirohabit_k.R
import com.e.envirohabit_k.model.UserModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var dl : DrawerLayout
    lateinit var abdt : ActionBarDrawerToggle
    //lateinit var points : TextView
    lateinit var welcomeMessage : TextView
    lateinit var userModel : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeMessage = findViewById(R.id.welcome_message)
        //points = findViewById(R.id.points_view)
        userModel = UserModel()
        userModel.getUserData {
            welcomeMessage.text = "Velkommen tilbake, ${it.username}"

        }

        dl = findViewById(R.id.dl)
        abdt = ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close)

        dl.addDrawerListener(abdt)
        abdt.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView = findViewById<NavigationView>(R.id.nav_view)

        navView.setNavigationItemSelectedListener {
            val id = it.itemId

            if (id == R.id.ny_miljohandling_item) {
                Toast.makeText(this, "Ny MiljøHandling", Toast.LENGTH_SHORT).show()
            }
            else if (id == R.id.kontooversikt_item) {
                Toast.makeText(this, "Kontooversikt", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AccountInfoActivity::class.java))
            }
            else if (id == R.id.statistikk_item) {
                Toast.makeText(this, "Statistikk", Toast.LENGTH_SHORT).show()
            }
            else if (id == R.id.instillinger_item) {
                Toast.makeText(this, "Instillinger", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SettingsActivity::class.java))
            }

            true
        }




    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }


}
