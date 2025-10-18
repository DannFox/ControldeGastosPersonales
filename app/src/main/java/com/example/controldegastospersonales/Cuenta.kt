package com.example.controldegastospersonales

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Cuenta(
    @SerializedName("id_cuenta")
    val idCuenta: Int,
    val nombre: String,
    val saldoInicial: Double,
    val saldoActual: Double,
    val gastos: List<Gasto>,
    val ingresos: List<Ingreso>
) : Serializable
