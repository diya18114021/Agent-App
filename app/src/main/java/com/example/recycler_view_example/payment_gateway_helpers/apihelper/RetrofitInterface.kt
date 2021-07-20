package com.example.recycler_view_example.payment_gateway_helpers.apihelper

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitInterface {
    @FormUrlEncoded
    @POST("/createorder")
    suspend fun createOrder(
        @Field("orderid") merchantOrderId: String,
        @Field("amount") amount: Double
    ): Response<Map<String, Any>>
}