package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNavView.selectedItemId = R.id.nav_accounts

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    val intent = Intent(this, MainActivity::class.java)
                    // Para evitar crear múltiples instancias de MainActivity si ya está en la pila
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish() // Termina AccountsActivity al ir a Home
                    true
                }
                R.id.nav_accounts -> {
                    // Ya estás aquí
                    true
                }
                R.id.nav_payments -> {
                    val intent = Intent(this, PaymentsActivity::class.java)
                    // Si PaymentsActivity puede estar encima de AccountsActivity, no necesitas flags especiales
                    // o podrías usar REORDER_TO_FRONT si quieres traerla al frente si ya existe.
                    startActivity(intent)
                    finish() // Termina AccountsActivity al ir a Payments
                    true
                }
                else -> false
            }
        }
    }
}