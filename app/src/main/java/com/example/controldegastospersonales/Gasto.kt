package com.example.controldegastospersonales

import com.google.gson.annotations.SerializedName

data class Gasto(
    @SerializedName("id_gasto")
    val idGasto: Int,
    val monto: Double,
    val descripcion: String,
    val fecha: String,
    val cuentaId: Int,
    val cuenta: Cuenta,
    val categoriaId: Int,
    val categoria: String?
)
