package com.example.shopease.data.model

data class CartItem(
    val id: String? = null,
    val user_id: String = "guest",
    val product_id: String,
    val product_name: String,
    val price: Double,
    val image_url: String?,
    val selected_color: String? = null,
    val selected_size: String? = null,
    val quantity: Int = 1
)