package com.rakib.sales_agents_attendance

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val baseURL = "http://128.199.215.102:4040"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create()) // Gson Converter Factory Converts JSON Object to Java Object
            .build()
    }
}