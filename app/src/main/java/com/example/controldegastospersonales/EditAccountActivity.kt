package com.example.controldegastospersonales

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.controldegastospersonales.API.APIClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAccountActivity : AppCompatActivity() {

    private lateinit var etAccountName: TextInputEditText
    private lateinit var etInitialBalance: TextInputEditText
    private lateinit var btnSaveChanges: Button
    private lateinit var btnClose: ImageButton
    private lateinit var cuenta: Cuenta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        etAccountName = findViewById(R.id.etAccountName)
        etInitialBalance = findViewById(R.id.etInitialBalance)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)
        btnClose = findViewById(R.id.btnClose)

        cuenta = intent.getSerializableExtra("cuenta") as Cuenta

        etAccountName.setText(cuenta.nombre)
        etInitialBalance.setText(cuenta.saldoInicial.toString())

        btnClose.setOnClickListener {
            finish()
        }

        btnSaveChanges.setOnClickListener {
            val accountName = etAccountName.text.toString()
            val initialBalance = etInitialBalance.text.toString().toDoubleOrNull()

            if (accountName.isNotEmpty() && initialBalance != null) {
                val updatedAccount = cuenta.copy(nombre = accountName, saldoInicial = initialBalance, saldoActual = initialBalance)
                updateAccount(updatedAccount)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAccount(account: Cuenta) {
        val call = APIClient.instance.updateCuenta(account.idCuenta, account)
        call.enqueue(object : Callback<Cuenta> {
            override fun onResponse(call: Call<Cuenta>, response: Response<Cuenta>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditAccountActivity, "Cuenta actualizada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditAccountActivity, "Error al actualizar la cuenta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Cuenta>, t: Throwable) {
                Toast.makeText(this@EditAccountActivity, "Fallo en la llamada a la API", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
