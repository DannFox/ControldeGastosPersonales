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

class AddAccountActivity : AppCompatActivity() {

    private lateinit var etAccountName: TextInputEditText
    private lateinit var etInitialBalance: TextInputEditText
    private lateinit var btnSaveAccount: Button
    private lateinit var btnClose: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        etAccountName = findViewById(R.id.etAccountName)
        etInitialBalance = findViewById(R.id.etInitialBalance)
        btnSaveAccount = findViewById(R.id.btnSaveAccount)
        btnClose = findViewById(R.id.btnClose)

        btnClose.setOnClickListener {
            finish()
        }

        btnSaveAccount.setOnClickListener {
            val accountName = etAccountName.text.toString()
            val initialBalance = etInitialBalance.text.toString().toDoubleOrNull()

            if (accountName.isNotEmpty() && initialBalance != null) {
                val newAccount = Cuenta(0, accountName, initialBalance, initialBalance, emptyList(), emptyList())
                createAccount(newAccount)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createAccount(account: Cuenta) {
        val call = APIClient.instance.addCuenta(account)
        call.enqueue(object : Callback<Cuenta> {
            override fun onResponse(call: Call<Cuenta>, response: Response<Cuenta>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddAccountActivity, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddAccountActivity, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Cuenta>, t: Throwable) {
                Toast.makeText(this@AddAccountActivity, "Fallo en la llamada a la API", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
