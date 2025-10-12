package com.example.controldegastospersonales.API

import com.example.controldegastospersonales.Categoria
import com.example.controldegastospersonales.Cuenta
import com.example.controldegastospersonales.Gasto
import com.example.controldegastospersonales.Ingreso
import retrofit2.Call
import retrofit2.http.GET

interface IAPIIService {
    //Metodo GET para las Categorias
    @GET("Categoria")
    fun getCategorias(): Call<List<Categoria>>

    //Metodo GET para las Cuentas
    @GET("Cuentas")
    fun getCuentas(): Call<List<Cuenta>>

    //Metodo GET para los Gastos
    @GET("Gastos")
    fun getGastos(): Call<List<Gasto>>

    //Metodo GET para los Ingresos
    @GET("Ingresos")
    fun getIngresos(): Call<List<Ingreso>>
}
