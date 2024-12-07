package com.example.bantalapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bantalapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment



class navbar : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding.bottomNavigationView =  findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationView()

        // Set default fragment
        replaceFragment(Mood())
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mood -> {
                    replaceFragment(Mood())
                    true
                }
                R.id.jurnal -> {
                    replaceFragment(Jurnal())
                    true
                }
                R.id.activity -> {
                    replaceFragment(Activity())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

}

