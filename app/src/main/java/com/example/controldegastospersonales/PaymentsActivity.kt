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

        // Marcar "Pagos" como seleccionado
        bottomNavView.selectedItemId = R.id.nav_payments

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish() // Termina PaymentsActivity al ir a Home
                    true
                }

                R.id.nav_accounts -> {
                    val intent = Intent(this, AccountsActivity::class.java)
                    startActivity(intent)
                    finish() // Termina PaymentsActivity al ir a Accounts
                    true
                }

                R.id.nav_payments -> {
                    // Ya estás aquí
                    true
                }

                else -> false
            }
        }
        bottomNavView.selectedItemId = R.id.nav_payments
    }
}