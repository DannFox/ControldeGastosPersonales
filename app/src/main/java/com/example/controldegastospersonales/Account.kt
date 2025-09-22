package com.example.controldegastospersonales

// Account.kt (en algún lugar de tu estructura de paquetes)
data class Account(
    val id: String, // O Int, como prefieras
    val name: String,
    val balance: Double,
    val currency: String,
    val symbolResId: Int, // ID de recurso para el drawable del símbolo/icono
    val colorHex: String   // Código de color hexadecimal (ej. "#FF5722")
)