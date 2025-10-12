package com.example.controldegastospersonales

import com.google.gson.annotations.SerializedName

data class Cuenta(
    @SerializedName("id_cuenta")
    val idCuenta: Int,
    val nombre: String,
    val saldoInicial: Double,
    val saldoActual: Double,
    val gastos: List<String>,
    val ingresos: List<String>
)
