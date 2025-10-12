package com.example.controldegastospersonales

import com.google.gson.annotations.SerializedName

data class Ingreso(
    @SerializedName("id_ingreso")
    val idIngreso: Int,
    val monto: Double,
    val descripcion: String,
    val fecha: String,
    val cuentaId: Int,
    val cuenta: Cuenta,
    val categoriaId: Int,
    val categoria: String?
)
