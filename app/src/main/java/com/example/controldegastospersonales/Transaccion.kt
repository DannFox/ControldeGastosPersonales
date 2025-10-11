package com.example.controldegastospersonales

data class Transaccion(
    val id: Int,
    val monto: Double,
    val descripcion: String,
    val fecha: String,
    val cuentaId: Int,
    val categoriaId: Int,
    val tipo: String // "gasto" o "ingreso"
)
