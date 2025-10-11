package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var rvGastosRecientes: RecyclerView
    private lateinit var gastoAdapter: GastoAdapter
    private var listaDeGastos: MutableList<Gasto> = mutableListOf()
    private var listaDeIngresos: MutableList<Ingreso> = mutableListOf()
    private lateinit var tvNoGastos: TextView
    private lateinit var tvBalanceTotal: TextView
    private lateinit var tvTotalGastos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNoGastos = findViewById(R.id.tvNoGastos)
        tvBalanceTotal = findViewById(R.id.tvBalanceTotal)
        tvTotalGastos = findViewById(R.id.tvTotalGastos)
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNavView.selectedItemId = R.id.nav_dashboard

        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    true
                }
                R.id.nav_accounts -> {
                    val intent = Intent(this, CuentasActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }
                R.id.nav_transactions -> {
                    val intent = Intent(this, TransaccionesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }
                R.id.nav_payments -> {
                    val intent = Intent(this, PagosActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
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

    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        return format.format(amount)
    }

    private fun loadDashboardData() {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        val gastosCall = apiService.getGastos()
        val ingresosCall = apiService.getIngresos()
        var finishedCalls = 0

        val onDataLoaded = { ->
            finishedCalls++
            if (finishedCalls == 2) {
                // Update Gastos Recientes RecyclerView
                gastoAdapter.notifyDataSetChanged()
                if (listaDeGastos.isEmpty()) {
                    tvNoGastos.visibility = View.VISIBLE
                    rvGastosRecientes.visibility = View.GONE
                } else {
                    tvNoGastos.visibility = View.GONE
                    rvGastosRecientes.visibility = View.VISIBLE
                }

                // Calculate and set totals
                val totalGastos = listaDeGastos.sumOf { it.Monto }
                val totalIngresos = listaDeIngresos.sumOf { it.Monto }
                val balanceTotal = totalIngresos - totalGastos

                tvTotalGastos.text = formatCurrency(totalGastos)
                tvBalanceTotal.text = formatCurrency(balanceTotal)
            }
        }

        gastosCall.enqueue(object : Callback<List<Gasto>> {
            override fun onResponse(call: Call<List<Gasto>>, response: Response<List<Gasto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        listaDeGastos.clear()
                        listaDeGastos.addAll(it)
                    }
                } else {
                    Log.e("API Error", "Error fetching gastos: ${response.code()}")
                }
                onDataLoaded()
            }

            override fun onFailure(call: Call<List<Gasto>>, t: Throwable) {
                Log.e("API Failure", "Error fetching gastos: ${t.message}", t)
                onDataLoaded()
            }
        })

        ingresosCall.enqueue(object : Callback<List<Ingreso>> {
            override fun onResponse(call: Call<List<Ingreso>>, response: Response<List<Ingreso>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        listaDeIngresos.clear()
                        listaDeIngresos.addAll(it)
                    }
                } else {
                    Log.e("API Error", "Error fetching ingresos: ${response.code()}")
                }
                onDataLoaded()
            }

            override fun onFailure(call: Call<List<Ingreso>>, t: Throwable) {
                Log.e("API Failure", "Error fetching ingresos: ${t.message}", t)
                onDataLoaded()
            }
        })
    }
}
