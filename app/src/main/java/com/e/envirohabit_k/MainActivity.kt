package com.e.envirohabit_k

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dl : DrawerLayout
    lateinit var abdt : ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dl = findViewById(R.id.dl)
        abdt = ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close)

        dl.addDrawerListener(abdt)
        abdt.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val nav_view = findViewById<NavigationView>(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener {
            val id = it.itemId

            if (id == R.id.ny_miljohandling_item) {
                Toast.makeText(this, "NyMiljoHandling", Toast.LENGTH_SHORT).show()
            }
            else if (id == R.id.kontooversikt_item) {
                Toast.makeText(this, "Kontooversikt", Toast.LENGTH_SHORT).show()
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
