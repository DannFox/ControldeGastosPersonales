package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaymentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNavView.selectedItemId = R.id.nav_payments

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.nav_accounts -> {
                    val intent = Intent(this, AccountsActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.nav_payments -> {
                    true
                }

                else -> false
            }
        }
        bottomNavView.selectedItemId = R.id.nav_payments
    }
}