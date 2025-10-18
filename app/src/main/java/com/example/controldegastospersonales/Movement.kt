package com.example.controldegastospersonales

import java.util.Date

enum class MovementType {
    INGRESO,
    GASTO
}

data class Movement(
    val id: Int,
    val description: String,
    val amount: Double,
    val date: Date,
    val type: MovementType,
    val categoryName: String
)
