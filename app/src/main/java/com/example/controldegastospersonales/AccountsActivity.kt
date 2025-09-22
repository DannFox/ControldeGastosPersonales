// AccountsActivity.kt
package com.example.controldegastospersonales
import AccountAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldegastospersonales.Account
import com.example.controldegastospersonales.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountsActivity : AppCompatActivity() {

    private lateinit var rvAccounts: RecyclerView
    private lateinit var accountAdapter: AccountAdapter
    private val sampleAccounts = mutableListOf<Account>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav) // Reemplaza con el ID real

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> { // Asume que tienes un item con id "navigation_home" en tu menú
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    true
                }

                R.id.nav_accounts -> { // Asume que tienes un item con id "navigation_accounts"
                    // Ya estás en AccountsActivity, podrías no hacer nada o refrescar
                    true
                }

                R.id.nav_payments -> { // Asume que tienes un item con id "navigation_payments"
                    val intent = Intent(this, PaymentsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }
                // Agrega más casos para otros items de navegación
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.nav_accounts

        rvAccounts = findViewById(R.id.rvAccounts)
        rvAccounts.layoutManager = LinearLayoutManager(this)

        // Cargar datos de ejemplo (esto es lo que verás en debug)
        loadSampleData()

        accountAdapter = AccountAdapter(sampleAccounts)
        rvAccounts.adapter = accountAdapter

        // ... resto de tu lógica para AccountsActivity
        // val btnAddAccount = findViewById<Button>(R.id.btnAddAccount)
        // btnAddAccount.setOnClickListener { /* ... */ }
    }

    private fun loadSampleData() {        // Aquí es donde "pones en duro" los datos para tiempo de ejecución (debug/release)
        sampleAccounts.add(
            Account(
                id = "1",
                name = "Ahorros Banco XYZ",
                balance = 1250.75,
                currency = "USD",
                symbolResId = R.drawable.baseline_account_balance_24, // Reemplaza con tus drawables
                colorHex = "#4CAF50" // Verde
            )
        )
        sampleAccounts.add(
            Account(
                id = "2",
                name = "Tarjeta de Crédito ABC",
                balance = -300.50, // Los saldos pueden ser negativos para créditos
                currency = "EUR",
                symbolResId = R.drawable.baseline_credit_card_24, // Reemplaza
                colorHex = "#2196F3" // Azul
            )
        )
        sampleAccounts.add(
            Account(
                id = "3",
                name = "Efectivo (Billetera)",
                balance = 85.00,
                currency = "USD",
                symbolResId = R.drawable.baseline_account_balance_wallet_24, // Reemplaza
                colorHex = "#FFC107" // Ámbar
            )
        )
        // Añade más cuentas si quieres
    }
}
