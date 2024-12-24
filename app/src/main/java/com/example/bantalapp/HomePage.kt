package com.example.bantalapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.bantalapp.ActivityFragment
import com.example.bantalapp.HomeFragment
import com.example.bantalapp.JurnalFragment
import com.example.bantalapp.MoodFragment
import com.example.bantalapp.ProfileFragment
import com.example.bantalapp.R
import com.example.bantalapp.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class HomePage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frameLayout: FrameLayout
    private val auth :FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        drawerLayout = findViewById(R.id.drawer_layout)
        bottomNavigationView = findViewById(R.id.bottomNavView)
        frameLayout = findViewById(R.id.frameLayout)



        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navMood -> {
                    loadFragment(MoodFragment())
                    true
                }
                R.id.navJurnal -> {
                    loadFragment(JurnalFragment())
                    true
                }
                R.id.navActivity -> {
                    loadFragment(ActivityFragment())
                    true
                }
                else -> false
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            navigationView.setCheckedItem(R.id.nav_home)
        }
        val fragmentName = intent.getStringExtra("FRAGMENT_NAME")
        if (fragmentName != null) {
            val fragment = when (fragmentName) {
                "HomeFragment" -> HomeFragment()
                "MoodFragment" -> MoodFragment()
                "JurnalFragment" -> JurnalFragment()
                "ActivityFragment" -> ActivityFragment()
                else -> null
            }
            if (fragment != null) {
                loadFragment(fragment)
            }
        } else {
            // Default fragment if none is specified
            loadFragment(HomeFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment())
            R.id.nav_profile -> startActivity(Intent(this, Profile::class.java))
            R.id.nav_settings -> startActivity(Intent(this, Settings::class.java))
            R.id.nav_logout ->
                startActivity(Intent(this, MainActivity::class.java))
            else -> return false
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
