package com.example.recycler_view_example

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "http://192.168.43.189:3000/"

interface ExampleApi {
    @GET("products")
    fun getProducts() : Call<List<ExampleItem>>

    companion object {
        operator fun invoke() : ExampleApi{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ExampleApi::class.java)
        }
    }
}