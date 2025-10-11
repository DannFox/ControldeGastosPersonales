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

class TransaccionesActivity : AppCompatActivity() {

    private lateinit var rvTransacciones: RecyclerView
    private lateinit var tvEmptyTransactions: TextView
    private lateinit var transaccionAdapter: TransaccionAdapter
    private val transaccionesList = mutableListOf<Transaccion>()
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transacciones)

        rvTransacciones = findViewById(R.id.rvTransacciones)
        tvEmptyTransactions = findViewById(R.id.tvEmptyTransactions)
        rvTransacciones.layoutManager = LinearLayoutManager(this)
        bottomNav = findViewById(R.id.bottomNav)

        transaccionAdapter = TransaccionAdapter(transaccionesList)
        rvTransacciones.adapter = transaccionAdapter

        updateEmptyView() // Show empty view initially
        loadTransacciones()

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_accounts -> {
                    startActivity(Intent(this, CuentasActivity::class.java))
                    true
                }
                R.id.nav_transactions -> {
                    // Already here
                    true
                }
                R.id.nav_payments -> {
                    startActivity(Intent(this, PagosActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun updateEmptyView() {
        if (transaccionesList.isEmpty()) {
            rvTransacciones.visibility = View.GONE
            tvEmptyTransactions.visibility = View.VISIBLE
        } else {
            rvTransacciones.visibility = View.VISIBLE
            tvEmptyTransactions.visibility = View.GONE
        }
    }

    private fun loadTransacciones() {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        val gastosCall = apiService.getGastos()
        val ingresosCall = apiService.getIngresos()

        var finishedCalls = 0

        val callback = fun() {
            finishedCalls++
            if (finishedCalls == 2) {
                transaccionesList.sortByDescending { it.fecha } // Sort by date
                transaccionAdapter.notifyDataSetChanged()
                updateEmptyView()
            }
        }

        gastosCall.enqueue(object : Callback<List<Gasto>> {
            override fun onResponse(call: Call<List<Gasto>>, response: Response<List<Gasto>>) {
                if (response.isSuccessful) {
                    val gastos = response.body()
                    if (gastos != null) {
                        val transaccionesGasto = gastos.map {
                            Transaccion(it.Id_gasto, it.Monto, it.Descripcion, it.Fecha, it.CuentaId, it.CategoriaId, "gasto")
                        }
                        transaccionesList.addAll(transaccionesGasto)
                    }
                } else {
                    Log.e("API Error", "Error fetching gastos: ${response.code()}")
                }
                callback()
            }

            override fun onFailure(call: Call<List<Gasto>>, t: Throwable) {
                Log.e("API Failure", "Error fetching gastos: ${t.message}", t)
                callback()
            }
        })

        ingresosCall.enqueue(object : Callback<List<Ingreso>> {
            override fun onResponse(call: Call<List<Ingreso>>, response: Response<List<Ingreso>>) {
                if (response.isSuccessful) {
                    val ingresos = response.body()
                    if (ingresos != null) {
                        val transaccionesIngreso = ingresos.map {
                            Transaccion(it.Id_ingreso, it.Monto, it.Descripcion, it.Fecha, it.CuentaId, it.CategoriaId, "ingreso")
                        }
                        transaccionesList.addAll(transaccionesIngreso)
                    }
                } else {
                    Log.e("API Error", "Error fetching ingresos: ${response.code()}")
                }
                callback()
            }

            override fun onFailure(call: Call<List<Ingreso>>, t: Throwable) {
                Log.e("API Failure", "Error fetching ingresos: ${t.message}", t)
                callback()
            }
        })
    }
}
