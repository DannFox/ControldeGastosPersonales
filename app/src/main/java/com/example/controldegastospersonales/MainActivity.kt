package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNavView.selectedItemId = R.id.nav_dashboard

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                true }
                R.id.nav_accounts -> {
                val intent = Intent(this, AccountsActivity::class.java)
                startActivity(intent)
                true
                }
                R.id.nav_payments -> {
                val intent = Intent(this, PaymentsActivity::class.java)
                startActivity(intent)
                true
                }
                else -> false
            }
        }

    }
}