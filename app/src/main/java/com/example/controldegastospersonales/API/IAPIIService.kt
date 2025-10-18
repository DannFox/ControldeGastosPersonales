package com.example.controldegastospersonales.API

import com.example.controldegastospersonales.Categoria
import com.example.controldegastospersonales.Cuenta
import com.example.controldegastospersonales.Gasto
import com.example.controldegastospersonales.Ingreso
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    //Metodo POST para las Cuentas
    @POST("Cuentas")
    fun addCuenta(@Body cuenta: Cuenta): Call<Cuenta>

    //Metodo PUT para las Cuentas
    @PUT("Cuentas/{id}")
    fun updateCuenta(@Path("id") id: Int, @Body cuenta: Cuenta): Call<Cuenta>

    //Metodo DELETE para las Cuentas
    @DELETE("Cuentas/{id}")
    fun deleteCuenta(@Path("id") id: Int): Call<Void>

    //Metodo POST para los Gastos
    @POST("Gastos")
    fun insertGasto(@Body gasto: Gasto): Call<Gasto>

    //Metodo POST para los Ingresos
    @POST("Ingresos")
    fun insertIngreso(@Body ingreso: Ingreso): Call<Ingreso>
}
