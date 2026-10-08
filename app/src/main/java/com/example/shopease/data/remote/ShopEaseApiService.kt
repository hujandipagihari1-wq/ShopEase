package com.example.shopease.data.remote

import com.example.shopease.SupabaseConfig
import com.example.shopease.data.model.CartItem
import com.example.shopease.data.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ShopEaseApiService {

    @Headers(
        "Accept: application/json",
        "apikey: ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}"
    )
    @GET("rest/v1/products")
    suspend fun getProducts(
        @Query("select") select: String = "*"
    ): Response<List<Product>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "apikey: ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}",
        "Prefer: return=representation"
    )
    @POST("rest/v1/cart")
    suspend fun addToCart(
        @Body cartItem: CartItem
    ): Response<List<CartItem>>

    @Headers(
        "Accept: application/json",
        "apikey: ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}"
    )
    @GET("rest/v1/cart")
    suspend fun getCartItems(
        @Query("user_id") userId: String = "eq.guest",
        @Query("select") select: String = "*"
    ): Response<List<CartItem>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "apikey: ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/cart")
    suspend fun updateCartQuantity(
        @Query("id") id: String,
        @Body quantity: Map<String, Int>
    ): Response<List<CartItem>>

    @Headers(
        "Accept: application/json",
        "apikey: ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}",
        "Authorization: Bearer ${SupabaseConfig.SUPABASE_PUBLISHABLE_KEY}"
    )
    @DELETE("rest/v1/cart")
    suspend fun deleteCartItem(
        @Query("id") id: String
    ): Response<Void>
}
