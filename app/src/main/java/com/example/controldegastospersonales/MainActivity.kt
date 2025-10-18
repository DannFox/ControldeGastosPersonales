package com.example.controldegastospersonales

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldegastospersonales.API.APIClient
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var rvGastosRecientes: RecyclerView
    private lateinit var gastoAdapter: GastoAdapter
    private var listaDeGastos: MutableList<Gasto> = mutableListOf()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var tvBalanceTotal: TextView
    private lateinit var tvTotalIngresos: TextView
    private lateinit var tvTotalGastos: TextView
    private lateinit var categoriaAdapter: CategoriaAdapter
    private var listaDeCategorias: MutableList<Categoria> = mutableListOf()
    private lateinit var gastosPieChart: PieChart
    private lateinit var ingresosPieChart: PieChart
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var chartSelector: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBalanceTotal = findViewById(R.id.tvBalanceTotal)
        tvTotalIngresos = findViewById(R.id.tvTotalIngresos)
        tvTotalGastos = findViewById(R.id.tvTotalGastos)
        gastosPieChart = findViewById(R.id.ivGraficoGastos)
        ingresosPieChart = findViewById(R.id.ivGraficoIngresos)
        viewFlipper = findViewById(R.id.viewFlipperCharts)
        chartSelector = findViewById(R.id.rgChartSelector)

        chartSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbGastos -> viewFlipper.displayedChild = 0
                R.id.rbIngresos -> viewFlipper.displayedChild = 1
            }
        }

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
                R.id.navigation_movements -> {
                    startActivity(Intent(this, MovementsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        findViewById<FloatingActionButton>(R.id.fabAddExpense).setOnClickListener {
            showAddOptionsDialog()
        }

        rvGastosRecientes = findViewById(R.id.rvGastosRecientes)
        rvGastosRecientes.layoutManager = LinearLayoutManager(this)
        gastoAdapter = GastoAdapter(listaDeGastos)
        rvGastosRecientes.adapter = gastoAdapter
        categoriaAdapter = CategoriaAdapter(listaDeCategorias)

        loadDashboardData()
    }

    private fun showAddOptionsDialog() {
        val options = arrayOf("Gasto", "Ingreso")
        AlertDialog.Builder(this)
            .setTitle("¿Qué deseas agregar?")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        startActivity(Intent(this, AddGastoActivity::class.java))
                    }
                    1 -> {
                        startActivity(Intent(this, AddIngresoActivity::class.java))
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun setupGastosPieChart(gastos: List<Gasto>, categorias: List<Categoria>, totalGastos: Double) {
        val gastosPorCategoria = gastos.groupBy { it.categoriaId }
            .mapValues { entry -> entry.value.sumOf { it.monto } }

        val pieEntries = ArrayList<PieEntry>()
        for ((categoriaId, total) in gastosPorCategoria) {
            val categoriaNombre = categorias.find { it.idCategoria == categoriaId }?.nombre ?: "Desconocida"
            pieEntries.add(PieEntry(total.toFloat(), categoriaNombre))
        }

        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = ColorTemplate.PASTEL_COLORS.toList()
        pieDataSet.valueTextSize = 12f
        pieDataSet.valueTextColor = Color.BLACK

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter(gastosPieChart))

        gastosPieChart.data = pieData
        gastosPieChart.description.isEnabled = false
        gastosPieChart.isDrawHoleEnabled = true
        gastosPieChart.holeRadius = 58f
        gastosPieChart.transparentCircleRadius = 61f
        gastosPieChart.setUsePercentValues(true)
        gastosPieChart.setDrawEntryLabels(false)

        gastosPieChart.centerText = "Total Gastos\n${String.format("$%,.2f", totalGastos)}"
        gastosPieChart.setCenterTextSize(18f)
        gastosPieChart.setCenterTextColor(Color.DKGRAY)

        val legend = gastosPieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.isWordWrapEnabled = true
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 5f

        gastosPieChart.animateY(1400, Easing.EaseInOutQuad)
        gastosPieChart.invalidate()
    }

    private fun setupIngresosPieChart(ingresos: List<Ingreso>, categorias: List<Categoria>, totalIngresos: Double) {
        val ingresosPorCategoria = ingresos.groupBy { it.categoriaId }
            .mapValues { entry -> entry.value.sumOf { it.monto } }

        val pieEntries = ArrayList<PieEntry>()
        for ((categoriaId, total) in ingresosPorCategoria) {
            val categoriaNombre = categorias.find { it.idCategoria == categoriaId }?.nombre ?: "Desconocida"
            pieEntries.add(PieEntry(total.toFloat(), categoriaNombre))
        }

        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        pieDataSet.valueTextSize = 12f
        pieDataSet.valueTextColor = Color.BLACK

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter(ingresosPieChart))

        ingresosPieChart.data = pieData
        ingresosPieChart.description.isEnabled = false
        ingresosPieChart.isDrawHoleEnabled = true
        ingresosPieChart.holeRadius = 58f
        ingresosPieChart.transparentCircleRadius = 61f
        ingresosPieChart.setUsePercentValues(true)
        ingresosPieChart.setDrawEntryLabels(false)

        ingresosPieChart.centerText = "Total Ingresos\n${String.format("$%,.2f", totalIngresos)}"
        ingresosPieChart.setCenterTextSize(18f)
        ingresosPieChart.setCenterTextColor(Color.DKGRAY)

        val legend = ingresosPieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.isWordWrapEnabled = true
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 5f

        ingresosPieChart.animateY(1400, Easing.EaseInOutQuad)
        ingresosPieChart.invalidate()
    }

    private fun loadDashboardData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accountsResponse = APIClient.instance.getCuentas().execute()
                val ingresosResponse = APIClient.instance.getIngresos().execute()
                val gastosResponse = APIClient.instance.getGastos().execute()
                val categoriesResponse = APIClient.instance.getCategorias().execute()

                withContext(Dispatchers.Main) {
                    val cuentas = if(accountsResponse.isSuccessful) accountsResponse.body() ?: emptyList() else emptyList()
                    val gastos = if (gastosResponse.isSuccessful) gastosResponse.body() ?: emptyList() else emptyList()
                    val ingresos = if (ingresosResponse.isSuccessful) ingresosResponse.body() ?: emptyList() else emptyList()
                    val categorias = if (categoriesResponse.isSuccessful) categoriesResponse.body() ?: emptyList() else emptyList()

                    val totalSaldoCuentas = cuentas.sumOf { it.saldoActual }
                    val totalGastos = gastos.sumOf { it.monto }
                    val totalIngresos = ingresos.sumOf { it.monto }
                    val balanceTotal = totalSaldoCuentas + totalIngresos - totalGastos

                    tvTotalIngresos.text = String.format("$%,.2f", totalIngresos)
                    tvTotalGastos.text = String.format("$%,.2f", totalGastos)
                    tvBalanceTotal.text = String.format("$%,.2f", balanceTotal)


                    if (gastosResponse.isSuccessful) {
                        listaDeGastos.clear()
                        listaDeGastos.addAll(gastos)
                        gastoAdapter.notifyDataSetChanged()
                    } else {
                        Log.e("MainActivity", "Error al obtener gastos: ${gastosResponse.errorBody()?.string()}")
                    }

                    if (categoriesResponse.isSuccessful) {
                        listaDeCategorias.clear()
                        listaDeCategorias.addAll(categorias)
                        categoriaAdapter.notifyDataSetChanged()
                    } else {
                        Log.e("MainActivity", "Error al obtener categorias: ${categoriesResponse.errorBody()?.string()}")
                    }

                    if (gastos.isNotEmpty() && categorias.isNotEmpty()) {
                        setupGastosPieChart(gastos, categorias, totalGastos)
                    }

                    if (ingresos.isNotEmpty() && categorias.isNotEmpty()) {
                        setupIngresosPieChart(ingresos, categorias, totalIngresos)
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
