package com.rakib.sales_agents_attendance

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            // Gson Converter Factory Converts JSON Object to Java Object
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}