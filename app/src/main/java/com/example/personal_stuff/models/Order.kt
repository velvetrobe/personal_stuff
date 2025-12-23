package com.example.personal_stuff.models

data class Order(
    val id: Int,
    val userId: Int,
    val items: List<OrderItem>,
    val totalPrice: Double,
    val totalQuantity: Int,
    val orderDate: String,
    val status: String
)

data class OrderItem(
    val productId: Int,
    val quantity: Int,
    val name: String,
    val price: Double,
    val imageUrl: String
)