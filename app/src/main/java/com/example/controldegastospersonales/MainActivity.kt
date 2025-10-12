package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldegastospersonales.API.APIClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var rvGastosRecientes: RecyclerView
    private lateinit var gastoAdapter: GastoAdapter
    private var listaDeGastos: MutableList<Gasto> = mutableListOf()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var tvBalanceTotal: TextView
    private lateinit var tvTotalGastos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBalanceTotal = findViewById(R.id.tvBalanceTotal)
        tvTotalGastos = findViewById(R.id.tvTotalGastos)

        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.selectedItemId = R.id.navigation_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_accounts -> {
                    startActivity(Intent(this, AccountsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_categories -> {
                    startActivity(Intent(this, CategoriesActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_payments -> {
                    startActivity(Intent(this, PaymentsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        rvGastosRecientes = findViewById(R.id.rvGastosRecientes)
        rvGastosRecientes.layoutManager = LinearLayoutManager(this)
        gastoAdapter = GastoAdapter(listaDeGastos)
        rvGastosRecientes.adapter = gastoAdapter

        loadDashboardData()
    }

    private fun loadDashboardData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accountsResponse = APIClient.instance.getCuentas().execute()
                val gastosResponse = APIClient.instance.getGastos().execute()

                withContext(Dispatchers.Main) {
                    if (accountsResponse.isSuccessful) {
                        val cuentas = accountsResponse.body() ?: emptyList()
                        val totalBalance = cuentas.sumOf { it.saldoActual }
                        tvBalanceTotal.text = String.format("$%,.2f", totalBalance)
                    } else {
                        Log.e("MainActivity", "Error al obtener cuentas: ${accountsResponse.errorBody()?.string()}")
                    }

                    if (gastosResponse.isSuccessful) {
                        val gastos = gastosResponse.body() ?: emptyList()
                        val totalGastos = gastos.sumOf { it.monto }
                        tvTotalGastos.text = String.format("$%,.2f", totalGastos)
                        
                        listaDeGastos.clear()
                        listaDeGastos.addAll(gastos)
                        gastoAdapter.notifyDataSetChanged()
                    } else {
                        Log.e("MainActivity", "Error al obtener gastos: ${gastosResponse.errorBody()?.string()}")
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    Log.e("MainActivity", "Fallo en la llamada a la API", t)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadDashboardData()
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }
}
