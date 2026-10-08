package com.example.shopease.data.remote

import com.example.shopease.SupabaseConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SupabaseConfig.SUPABASE_URL + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ShopEaseApiService by lazy {
        retrofit.create(ShopEaseApiService::class.java)
    }
}