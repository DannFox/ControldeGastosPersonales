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

class AddIngresoActivity : AppCompatActivity() {

    private lateinit var etIngresoMonto: TextInputEditText
    private lateinit var etIngresoDescripcion: TextInputEditText
    private lateinit var spinnerIngresoCuenta: Spinner
    private lateinit var spinnerIngresoCategoria: Spinner
    private lateinit var btnGuardarIngreso: Button

    private var listaDeCuentas: MutableList<Cuenta> = mutableListOf()
    private var listaDeCategorias: MutableList<Categoria> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingreso)

        etIngresoMonto = findViewById(R.id.etIngresoMonto)
        etIngresoDescripcion = findViewById(R.id.etIngresoDescripcion)
        spinnerIngresoCuenta = findViewById(R.id.spinnerIngresoCuenta)
        spinnerIngresoCategoria = findViewById(R.id.spinnerIngresoCategoria)
        btnGuardarIngreso = findViewById(R.id.btnGuardarIngreso)

        loadSpinnerData()

        btnGuardarIngreso.setOnClickListener {
            saveIngreso()
        }
    }

    private fun loadSpinnerData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cuentasResponse = APIClient.instance.getCuentas().execute()
                val categoriasResponse = APIClient.instance.getCategorias().execute()

                withContext(Dispatchers.Main) {
                    // Spinner de cuentas
                    if (cuentasResponse.isSuccessful) {
                        listaDeCuentas.addAll(cuentasResponse.body() ?: emptyList())
                        val adapterCuentas = ArrayAdapter(
                            this@AddIngresoActivity,
                            android.R.layout.simple_spinner_item,
                            listaDeCuentas.map { it.nombre }
                        )
                        adapterCuentas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerIngresoCuenta.adapter = adapterCuentas
                    } else {
                        Log.e("AddIngresoActivity", "Error al obtener cuentas: ${cuentasResponse.errorBody()?.string()}")
                    }

                    // Spinner de categorías
                    if (categoriasResponse.isSuccessful) {
                        val categoriasIngreso = categoriasResponse.body()?.filter { it.tipo == "Ingreso" } ?: emptyList()
                        listaDeCategorias.addAll(categoriasIngreso)
                        val adapterCategorias = ArrayAdapter(
                            this@AddIngresoActivity,
                            android.R.layout.simple_spinner_item,
                            listaDeCategorias.map { it.nombre }
                        )
                        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerIngresoCategoria.adapter = adapterCategorias
                    } else {
                        Log.e("AddIngresoActivity", "Error al obtener categorías: ${categoriasResponse.errorBody()?.string()}")
                    }
                }
            } catch (t: Throwable) {
                Log.e("AddIngresoActivity", "Fallo en la llamada a la API", t)
            }
        }
    }

    private fun saveIngreso() {
        val monto = etIngresoMonto.text.toString().toDoubleOrNull()
        val descripcion = etIngresoDescripcion.text.toString()
        val cuentaNombre = spinnerIngresoCuenta.selectedItem as? String
        val categoriaNombre = spinnerIngresoCategoria.selectedItem as? String

        val cuentaSeleccionada = listaDeCuentas.find { it.nombre == cuentaNombre }
        val categoriaSeleccionada = listaDeCategorias.find { it.nombre == categoriaNombre }

        if (monto == null || monto <= 0) {
            Toast.makeText(this, "Por favor, introduce un monto válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (descripcion.isBlank()) {
            Toast.makeText(this, "Por favor, introduce una descripción", Toast.LENGTH_SHORT).show()
            return
        }

        if (cuentaSeleccionada == null) {
            Toast.makeText(this, "Por favor, selecciona una cuenta", Toast.LENGTH_SHORT).show()
            return
        }

        if (categoriaSeleccionada == null) {
            Toast.makeText(this, "Por favor, selecciona una categoría", Toast.LENGTH_SHORT).show()
            return
        }

        // Formatear fecha en formato ISO
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val fechaString = sdf.format(Date())

        val nuevoIngreso = Ingreso(
            idIngreso = 0,
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
                val response = APIClient.instance.insertIngreso(nuevoIngreso).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddIngresoActivity, "Ingreso guardado correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddIngresoActivity, "Error al guardar el ingreso: ${response.message()}", Toast.LENGTH_LONG).show()
                        Log.e("AddIngresoActivity", "Error al guardar el ingreso: ${response.errorBody()?.string()}")
                    }
                }
            } catch (t: Throwable) {
                Log.e("AddIngresoActivity", "Fallo en la llamada a la API para guardar ingreso", t)
            }
        }
    }
}
