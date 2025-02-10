package com.example.dorak.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dorak.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Clear all previous fragments inside nav_home
                    navController.popBackStack(R.id.nav_home, true)
                    navController.navigate(R.id.nav_home)
                    true
                }
                R.id.nav_my_ticket -> {
                    // Navigate to nav_my_ticket and clear home back stack
                    navController.popBackStack(R.id.nav_home, true)
                    navController.navigate(R.id.nav_my_ticket)
                    true
                }
                R.id.nav_profile -> {
                    // Navigate to nav_my_ticket and clear home back stack
                    navController.popBackStack(R.id.nav_home, true)
                    navController.navigate(R.id.nav_profile)
                    true
                }
                R.id.nav_more -> {
                    // Navigate to nav_my_ticket and clear home back stack
                    navController.popBackStack(R.id.nav_home, true)
                    navController.navigate(R.id.nav_more)
                    true
                }


                else -> false
            }
        }
    }
}


