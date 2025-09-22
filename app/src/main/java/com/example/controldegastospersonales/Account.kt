package com.example.controldegastospersonales

data class Account(
    val id: String,
    val name: String,
    val balance: Double,
    val currency: String,
    val symbolResId: Int,
    val colorHex: String
)