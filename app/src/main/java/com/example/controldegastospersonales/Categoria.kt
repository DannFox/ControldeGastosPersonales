package com.example.controldegastospersonales

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Categoria(
    @SerializedName("id_categoria")
    val idCategoria: Int,
    val nombre: String,
    val tipo: String,
    val esPredefinida: Boolean,
    val gastos: List<Gasto>,
    val ingresos: List<Ingreso>
) : Serializable
