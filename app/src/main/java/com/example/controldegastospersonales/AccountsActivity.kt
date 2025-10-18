package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldegastospersonales.API.APIClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountsActivity : AppCompatActivity() {

    private lateinit var rvCuentas: RecyclerView
    private lateinit var cuentaAdapter: CuentaAdapter
    private var listaDeCuentas: MutableList<Cuenta> = mutableListOf()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fabAddAccount: FloatingActionButton
    private lateinit var llEmptyAccounts: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        llEmptyAccounts = findViewById(R.id.llEmptyAccounts)
        rvCuentas = findViewById(R.id.rvCuentas)

        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.selectedItemId = R.id.navigation_accounts

        fabAddAccount = findViewById(R.id.fabAddAccount)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_accounts -> {
                    true
                }
                R.id.navigation_categories -> {
                    startActivity(Intent(this, CategoriesActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_movements -> {
                    startActivity(Intent(this, MovementsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        fabAddAccount.setOnClickListener {
            val intent = Intent(this, AddAccountActivity::class.java)
            startActivity(intent)
        }

        rvCuentas.layoutManager = LinearLayoutManager(this)
        cuentaAdapter = CuentaAdapter(listaDeCuentas)
        rvCuentas.adapter = cuentaAdapter

        loadCuentas()
    }

    private fun loadCuentas() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cuentasResponse = APIClient.instance.getCuentas().execute()
                val categoriasResponse = APIClient.instance.getCategorias().execute()

                withContext(Dispatchers.Main) {
                    if (cuentasResponse.isSuccessful && categoriasResponse.isSuccessful) {
                        val cuentas = cuentasResponse.body() ?: emptyList()
                        val categorias = categoriasResponse.body() ?: emptyList()
                        val categoriasMap = categorias.associateBy { it.idCategoria }

                        val hydratedCuentas = cuentas.map { cuenta ->
                            val hydratedGastos = cuenta.gastos.map { gasto ->
                                gasto.copy(categoria = categoriasMap[gasto.categoriaId])
                            }
                            val hydratedIngresos = cuenta.ingresos.map { ingreso ->
                                ingreso.copy(categoria = categoriasMap[ingreso.categoriaId])
                            }
                            cuenta.copy(gastos = hydratedGastos, ingresos = hydratedIngresos)
                        }

                        listaDeCuentas.clear()
                        listaDeCuentas.addAll(hydratedCuentas)
                        cuentaAdapter.notifyDataSetChanged()

                        if (listaDeCuentas.isEmpty()) {
                            rvCuentas.visibility = View.GONE
                            llEmptyAccounts.visibility = View.VISIBLE
                        } else {
                            rvCuentas.visibility = View.VISIBLE
                            llEmptyAccounts.visibility = View.GONE
                        }
                    } else {
                        Log.e("AccountsActivity", "Error al cargar datos desde la API")
                        rvCuentas.visibility = View.GONE
                        llEmptyAccounts.visibility = View.VISIBLE
                    }
                }
            } catch (t: Throwable) {
                Log.e("AccountsActivity", "Fallo en la llamada a la API", t)
                withContext(Dispatchers.Main) {
                    rvCuentas.visibility = View.GONE
                    llEmptyAccounts.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCuentas()
        bottomNavigationView.selectedItemId = R.id.navigation_accounts
    }
}
