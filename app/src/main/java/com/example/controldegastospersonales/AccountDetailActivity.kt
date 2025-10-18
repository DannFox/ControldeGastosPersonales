package com.example.controldegastospersonales

import android.content.Intent
import android.os.Bundle
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

    private lateinit var cuenta: Cuenta
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

        cuenta = intent.getSerializableExtra("cuenta") as Cuenta

        tvAccountName = findViewById(R.id.tvAccountNameDetail)
        tvAccountBalance = findViewById(R.id.tvAccountBalanceDetail)
        btnEdit = findViewById(R.id.btnEditAccountDetail)
        btnDelete = findViewById(R.id.btnDeleteAccountDetail)
        rvIngresos = findViewById(R.id.rvIngresos)
        rvGastos = findViewById(R.id.rvGastos)
        llEmptyIngresos = findViewById(R.id.llEmptyIngresos)
        llEmptyGastos = findViewById(R.id.llEmptyGastos)

        tvAccountName.text = cuenta.nombre
        tvAccountBalance.text = String.format("$%,.2f", cuenta.saldoActual)

        rvIngresos.layoutManager = LinearLayoutManager(this)
        rvGastos.layoutManager = LinearLayoutManager(this)

        loadIngresosAndGastos()

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

    private fun loadIngresosAndGastos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ingresosResponse = APIClient.instance.getIngresos().execute()
                val gastosResponse = APIClient.instance.getGastos().execute()

                withContext(Dispatchers.Main) {
                    if (ingresosResponse.isSuccessful) {
                        val allIngresos = ingresosResponse.body() ?: emptyList()
                        val filteredIngresos = allIngresos.filter { it.cuentaId == cuenta.idCuenta }
                        ingresoAdapter = IngresoAdapter(filteredIngresos)
                        rvIngresos.adapter = ingresoAdapter
                        
                        if (filteredIngresos.isEmpty()) {
                            rvIngresos.visibility = View.GONE
                            llEmptyIngresos.visibility = View.VISIBLE
                        } else {
                            rvIngresos.visibility = View.VISIBLE
                            llEmptyIngresos.visibility = View.GONE
                        }
                    }

                    if (gastosResponse.isSuccessful) {
                        val allGastos = gastosResponse.body() ?: emptyList()
                        val filteredGastos = allGastos.filter { it.cuentaId == cuenta.idCuenta }
                        gastoAdapter = GastoAdapter(filteredGastos)
                        rvGastos.adapter = gastoAdapter

                        if (filteredGastos.isEmpty()) {
                            rvGastos.visibility = View.GONE
                            llEmptyGastos.visibility = View.VISIBLE
                        } else {
                            rvGastos.visibility = View.VISIBLE
                            llEmptyGastos.visibility = View.GONE
                        }
                    }
                }
            } catch (t: Throwable) {
                // Handle error
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Cuenta")
            .setMessage("¿Estás seguro de que quieres eliminar esta cuenta? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> deleteAccount() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteAccount() {
        val call = APIClient.instance.deleteCuenta(cuenta.idCuenta)
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
        // Refresh data if coming back from edit screen
        val call = APIClient.instance.getCuentas()
        call.enqueue(object : Callback<List<Cuenta>> {
            override fun onResponse(call: Call<List<Cuenta>>, response: Response<List<Cuenta>>) {
                if (response.isSuccessful) {
                    val updatedCuentas = response.body() ?: emptyList()
                    val updatedCuenta = updatedCuentas.find { it.idCuenta == cuenta.idCuenta }
                    if (updatedCuenta != null) {
                        cuenta = updatedCuenta
                        tvAccountName.text = cuenta.nombre
                        tvAccountBalance.text = String.format("$%,.2f", cuenta.saldoActual)
                        loadIngresosAndGastos()
                    }
                }
            }

            override fun onFailure(call: Call<List<Cuenta>>, t: Throwable) {
                // Handle error
            }
        })
    }
}
