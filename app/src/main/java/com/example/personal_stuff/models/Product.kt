package com.example.personal_stuff.models

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    val imageUrl: String
)