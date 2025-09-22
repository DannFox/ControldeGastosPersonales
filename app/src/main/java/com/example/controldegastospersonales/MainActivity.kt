package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var rvGastosRecientes: RecyclerView
    private lateinit var gastoAdapter: GastoAdapter
    private var listaDeGastos: MutableList<Gasto> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNavView.selectedItemId = R.id.nav_dashboard

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                true }
                R.id.nav_accounts -> {
                val intent = Intent(this, AccountsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
                }
                R.id.nav_payments -> {
                val intent = Intent(this, PaymentsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
                }
                else -> false
            }
        }
        bottomNavView.selectedItemId = R.id.nav_dashboard

        rvGastosRecientes = findViewById(R.id.rvGastosRecientes)

        rvGastosRecientes.layoutManager = LinearLayoutManager(this)

        cargarGastos()

        gastoAdapter = GastoAdapter(listaDeGastos)
        rvGastosRecientes.adapter = gastoAdapter

    }

    private fun cargarGastos() {
        listaDeGastos.add (
            Gasto(
                id = UUID.randomUUID().toString(),
                nombre = "Café matutino",
                monto = 2.75,
                fecha = "24 Oct 2023",
                iconoCategoriaResId = R.drawable.ic_category_placeholder
            )
        )
        listaDeGastos.add(
            Gasto(
                id = UUID.randomUUID().toString(),
                nombre = "Almuerzo de trabajo",
                monto = 12.50,
                fecha = "24 Oct 2023",
                iconoCategoriaResId = R.drawable.ic_category_placeholder
            )
        )
        listaDeGastos.add(
            Gasto(
                id = UUID.randomUUID().toString(),
                nombre = "Supermercado Semanal",
                monto = 85.30,
                fecha = "23 Oct 2023",
                iconoCategoriaResId = R.drawable.ic_category_placeholder
            )
        )
        listaDeGastos.add(
            Gasto(
                id = UUID.randomUUID().toString(),
                nombre = "Factura de Internet",
                monto = 55.00,
                fecha = "22 Oct 2023",
                iconoCategoriaResId = R.drawable.ic_category_placeholder
            )
        )
        listaDeGastos.add(
            Gasto(
                id = UUID.randomUUID().toString(),
                nombre = "Libro de Programación",
                monto = 30.99,
                fecha = "21 Oct 2023",
                iconoCategoriaResId = R.drawable.ic_category_placeholder
            )
        )
    }
}