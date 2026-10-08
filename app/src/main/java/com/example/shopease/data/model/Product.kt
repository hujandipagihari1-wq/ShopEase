package com.example.shopease.data.model

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val image_url: String?,
    val description: String?,
    val stock: Int,
    val variants: Map<String, Any>?
)
