package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldegastospersonales.API.APIClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountDetailActivity : AppCompatActivity() {

    private var cuenta: Cuenta? = null
    private lateinit var tvAccountName: TextView
    private lateinit var tvAccountBalance: TextView
    private lateinit var btnEdit: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var rvIngresos: RecyclerView
    private lateinit var rvGastos: RecyclerView
    private lateinit var ingresoAdapter: IngresoAdapter
    private lateinit var gastoAdapter: GastoAdapter
    private lateinit var llEmptyIngresos: LinearLayout
    private lateinit var llEmptyGastos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)

        cuenta = intent.getSerializableExtra("cuenta") as? Cuenta

        if (cuenta == null) {
            finish()
            return
        }

        tvAccountName = findViewById(R.id.tvAccountNameDetail)
        tvAccountBalance = findViewById(R.id.tvAccountBalanceDetail)
        btnEdit = findViewById(R.id.btnEditAccountDetail)
        btnDelete = findViewById(R.id.btnDeleteAccountDetail)
        rvIngresos = findViewById(R.id.rvIngresos)
        rvGastos = findViewById(R.id.rvGastos)
        llEmptyIngresos = findViewById(R.id.llEmptyIngresos)
        llEmptyGastos = findViewById(R.id.llEmptyGastos)

        rvIngresos.layoutManager = LinearLayoutManager(this)
        rvGastos.layoutManager = LinearLayoutManager(this)

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditAccountActivity::class.java).apply {
                putExtra("cuenta", cuenta)
            }
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun loadAccountDetails() {
        val currentCuenta = cuenta ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cuentaResponse = APIClient.instance.getCuenta(currentCuenta.idCuenta).execute()
                val ingresosResponse = APIClient.instance.getIngresos().execute()
                val gastosResponse = APIClient.instance.getGastos().execute()

                withContext(Dispatchers.Main) {
                    if (cuentaResponse.isSuccessful) {
                        val fetchedCuenta = cuentaResponse.body()!!
                        cuenta = fetchedCuenta // Update the local cuenta object
                        tvAccountName.text = fetchedCuenta.nombre

                        val allIngresos = if (ingresosResponse.isSuccessful) ingresosResponse.body() ?: emptyList() else emptyList()
                        val filteredIngresos = allIngresos.filter { it.cuentaId == currentCuenta.idCuenta }
                        ingresoAdapter = IngresoAdapter(filteredIngresos)
                        rvIngresos.adapter = ingresoAdapter
                        llEmptyIngresos.visibility = if (filteredIngresos.isEmpty()) View.VISIBLE else View.GONE
                        rvIngresos.visibility = if (filteredIngresos.isEmpty()) View.GONE else View.VISIBLE

                        val allGastos = if (gastosResponse.isSuccessful) gastosResponse.body() ?: emptyList() else emptyList()
                        val filteredGastos = allGastos.filter { it.cuentaId == currentCuenta.idCuenta }
                        gastoAdapter = GastoAdapter(filteredGastos)
                        rvGastos.adapter = gastoAdapter
                        llEmptyGastos.visibility = if (filteredGastos.isEmpty()) View.VISIBLE else View.GONE
                        rvGastos.visibility = if (filteredGastos.isEmpty()) View.GONE else View.VISIBLE

                        val totalIngresos = filteredIngresos.sumOf { it.monto }
                        val totalGastos = filteredGastos.sumOf { it.monto }
                        val saldoActual = fetchedCuenta.saldoInicial + totalIngresos - totalGastos
                        tvAccountBalance.text = String.format("$%,.2f", saldoActual)

                    } else {
                        Toast.makeText(this@AccountDetailActivity, "Error al cargar los detalles de la cuenta", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (t: Throwable) {
                Log.e("AccountDetailActivity", "Failed to load account details", t)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AccountDetailActivity, "Fallo en la conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val currentCuenta = cuenta ?: return
        AlertDialog.Builder(this)
            .setTitle("Eliminar Cuenta")
            .setMessage("¿Estás seguro de que quieres eliminar esta cuenta? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> deleteAccount(currentCuenta.idCuenta) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteAccount(idCuenta: Int) {
        val call = APIClient.instance.deleteCuenta(idCuenta)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AccountDetailActivity, "Cuenta eliminada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AccountDetailActivity, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AccountDetailActivity, "Fallo en la llamada a la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadAccountDetails()
    }
}
