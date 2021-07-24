package com.example.recycler_view_example

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "http://192.168.43.189:3000/"

interface HomeApi {
    @GET("products")
    fun getProducts() : Call<List<HomeItem>>

    companion object {
        operator fun invoke() : HomeApi{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HomeApi::class.java)
        }
    }
}