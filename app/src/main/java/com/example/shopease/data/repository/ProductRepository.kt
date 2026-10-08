package com.example.shopease.data.repository

import com.example.shopease.data.model.Product
import com.example.shopease.data.remote.RetrofitClient

class ProductRepository {

    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = RetrofitClient.api.getProducts()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("Gagal mengambil produk: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}