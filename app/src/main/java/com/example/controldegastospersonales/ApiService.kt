package com.example.controldegastospersonales

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/gastos")
    fun getGastos(): Call<List<Gasto>>

    @GET("api/ingresos")
    fun getIngresos(): Call<List<Ingreso>>

    @GET("api/cuentas")
    fun getCuentas(): Call<List<Cuenta>>
}
