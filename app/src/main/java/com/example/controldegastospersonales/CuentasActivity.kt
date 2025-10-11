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

class CuentasActivity : AppCompatActivity() {

    private lateinit var rvAccounts: RecyclerView
    private lateinit var cuentaAdapter: CuentaAdapter
    private var accountsList = mutableListOf<Cuenta>()
    private lateinit var emptyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }

                R.id.nav_accounts -> {
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
        bottomNavigationView.selectedItemId = R.id.nav_accounts

        rvAccounts = findViewById(R.id.rvAccounts)
        emptyView = findViewById(R.id.empty_view)
        rvAccounts.layoutManager = LinearLayoutManager(this)

        // Set up the adapter with an empty list initially
        cuentaAdapter = CuentaAdapter(accountsList)
        rvAccounts.adapter = cuentaAdapter

        loadAccountsFromApi()
    }

    private fun loadAccountsFromApi() {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        val call = apiService.getCuentas()

        call.enqueue(object : Callback<List<Cuenta>> {
            override fun onResponse(call: Call<List<Cuenta>>, response: Response<List<Cuenta>>) {
                if (response.isSuccessful) {
                    val accounts = response.body()
                    if (accounts != null && accounts.isNotEmpty()) {
                        accountsList.clear()
                        accountsList.addAll(accounts)
                        cuentaAdapter.notifyDataSetChanged()
                        rvAccounts.visibility = View.VISIBLE
                        emptyView.visibility = View.GONE
                    } else {
                        accountsList.clear()
                        cuentaAdapter.notifyDataSetChanged()
                        rvAccounts.visibility = View.GONE
                        emptyView.visibility = View.VISIBLE
                    }
                } else {
                    Log.e("API Error", "Error: ${response.code()}")
                    accountsList.clear()
                    cuentaAdapter.notifyDataSetChanged()
                    rvAccounts.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Cuenta>>, t: Throwable) {
                Log.e("API Failure", "Error: ${t.message}", t)
                accountsList.clear()
                cuentaAdapter.notifyDataSetChanged()
                rvAccounts.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            }
        })
    }
}
