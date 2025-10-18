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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class MovementsActivity : AppCompatActivity() {

    private lateinit var rvMovements: RecyclerView
    private lateinit var movementsAdapter: MovementsAdapter
    private var allMovements: MutableList<Movement> = mutableListOf()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var llEmptyMovements: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movements)

        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.selectedItemId = R.id.navigation_movements

        rvMovements = findViewById(R.id.rvMovements)
        llEmptyMovements = findViewById(R.id.llEmptyMovements)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
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
                R.id.navigation_movements -> {
                    true
                }
                else -> false
            }
        }

        rvMovements.layoutManager = LinearLayoutManager(this)
        movementsAdapter = MovementsAdapter(allMovements)
        rvMovements.adapter = movementsAdapter

        loadAllMovements()
    }

    private fun loadAllMovements() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ingresosResponse = APIClient.instance.getIngresos().execute()
                val gastosResponse = APIClient.instance.getGastos().execute()
                val categoriasResponse = APIClient.instance.getCategorias().execute()
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

                withContext(Dispatchers.Main) {
                    allMovements.clear()

                    val categoriasMap = if (categoriasResponse.isSuccessful) {
                        (categoriasResponse.body() ?: emptyList()).associateBy({ it.idCategoria }, { it.nombre })
                    } else {
                        Log.e("MovementsActivity", "Error al obtener categorías: ${categoriasResponse.errorBody()?.string()}")
                        emptyMap<Int, String>()
                    }

                    if (ingresosResponse.isSuccessful) {
                        val ingresos = ingresosResponse.body() ?: emptyList()
                        allMovements.addAll(ingresos.map { 
                            val categoriaNombre = categoriasMap[it.categoriaId] ?: "Sin categoría"
                            Movement(it.idIngreso, it.descripcion, it.monto, parser.parse(it.fecha), MovementType.INGRESO, categoriaNombre)
                        })
                    } else {
                         Log.e("MovementsActivity", "Error al obtener ingresos: ${ingresosResponse.errorBody()?.string()}")
                    }

                    if (gastosResponse.isSuccessful) {
                        val gastos = gastosResponse.body() ?: emptyList()
                        allMovements.addAll(gastos.map { 
                            val categoriaNombre = categoriasMap[it.categoriaId] ?: "Sin categoría"
                            Movement(it.idGasto, it.descripcion, it.monto, parser.parse(it.fecha), MovementType.GASTO, categoriaNombre)
                        })
                    } else {
                        Log.e("MovementsActivity", "Error al obtener gastos: ${gastosResponse.errorBody()?.string()}")
                    }

                    allMovements.sortByDescending { it.date }
                    movementsAdapter.notifyDataSetChanged()

                    if (allMovements.isEmpty()) {
                        rvMovements.visibility = View.GONE
                        llEmptyMovements.visibility = View.VISIBLE
                    } else {
                        rvMovements.visibility = View.VISIBLE
                        llEmptyMovements.visibility = View.GONE
                    }
                }
            } catch (t: Throwable) {
                Log.e("MovementsActivity", "Fallo en la llamada a la API", t)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadAllMovements()
        bottomNavigationView.selectedItemId = R.id.navigation_movements
    }
}
