package com.example.controldegastospersonales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.controldegastospersonales.API.APIClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddGastoActivity : AppCompatActivity() {

    private lateinit var etGastoMonto: TextInputEditText
    private lateinit var etGastoDescripcion: TextInputEditText
    private lateinit var spinnerGastoCategoria: Spinner
    private lateinit var spinnerGastoCuenta: Spinner
    private lateinit var btnGuardarGasto: Button

    private var listaDeCategorias: MutableList<Categoria> = mutableListOf()
    private var listaDeCuentas: MutableList<Cuenta> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gasto)

        etGastoMonto = findViewById(R.id.etGastoMonto)
        etGastoDescripcion = findViewById(R.id.etGastoDescripcion)
        spinnerGastoCategoria = findViewById(R.id.spinnerGastoCategoria)
        spinnerGastoCuenta = findViewById(R.id.spinnerGastoCuenta)
        btnGuardarGasto = findViewById(R.id.btnGuardarGasto)

        loadSpinnerData()

        btnGuardarGasto.setOnClickListener {
            saveGasto()
        }
    }

    private fun loadSpinnerData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categoriesResponse = APIClient.instance.getCategorias().execute()
                val accountsResponse = APIClient.instance.getCuentas().execute()

                withContext(Dispatchers.Main) {
                    if (categoriesResponse.isSuccessful) {
                        val categoriasGasto = categoriesResponse.body()?.filter { it.tipo == "Gasto" } ?: emptyList()
                        listaDeCategorias.addAll(categoriasGasto)
                        val adapter = ArrayAdapter(this@AddGastoActivity, android.R.layout.simple_spinner_item, listaDeCategorias.map { it.nombre })
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerGastoCategoria.adapter = adapter
                    } else {
                        Log.e("AddGastoActivity", "Error al obtener categorias: ${categoriesResponse.errorBody()?.string()}")
                    }

                    if (accountsResponse.isSuccessful) {
                        listaDeCuentas.addAll(accountsResponse.body() ?: emptyList())
                        val adapter = ArrayAdapter(this@AddGastoActivity, android.R.layout.simple_spinner_item, listaDeCuentas.map { it.nombre })
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerGastoCuenta.adapter = adapter
                    } else {
                        Log.e("AddGastoActivity", "Error al obtener cuentas: ${accountsResponse.errorBody()?.string()}")
                    }
                }
            } catch (t: Throwable) {
                Log.e("AddGastoActivity", "Fallo en la llamada a la API", t)
            }
        }
    }

    private fun saveGasto() {
        val monto = etGastoMonto.text.toString().toDoubleOrNull()
        val descripcion = etGastoDescripcion.text.toString()
        val categoriaNombre = spinnerGastoCategoria.selectedItem as? String
        val categoriaSeleccionada = listaDeCategorias.find { it.nombre == categoriaNombre }
        val cuentaNombre = spinnerGastoCuenta.selectedItem as? String
        val cuentaSeleccionada = listaDeCuentas.find { it.nombre == cuentaNombre }


        if (monto == null || monto <= 0) {
            Toast.makeText(this, "Por favor, introduce un monto válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (descripcion.isBlank()) {
            Toast.makeText(this, "Por favor, introduce una descripción", Toast.LENGTH_SHORT).show()
            return
        }

        if (categoriaSeleccionada == null) {
            Toast.makeText(this, "Por favor, selecciona una categoría", Toast.LENGTH_SHORT).show()
            return
        }
        if (cuentaSeleccionada == null) {
            Toast.makeText(this, "Por favor, selecciona una cuenta", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val fechaString = sdf.format(Date())


        val nuevoGasto = Gasto(
            idGasto = 0,
            monto = monto,
            descripcion = descripcion,
            fecha = fechaString,
            cuentaId = cuentaSeleccionada.idCuenta,
            cuenta = null,
            categoriaId = categoriaSeleccionada.idCategoria,
            categoria = null
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = APIClient.instance.insertGasto(nuevoGasto).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddGastoActivity, "Gasto guardado correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddGastoActivity, "Error al guardar el gasto: ${response.message()}", Toast.LENGTH_LONG).show()
                        Log.e("AddGastoActivity", "Error al guardar el gasto: ${response.errorBody()?.string()}")
                    }
                }
            } catch (t: Throwable) {
                Log.e("AddGastoActivity", "Fallo en la llamada a la API para guardar gasto", t)
            }
        }
    }
}
