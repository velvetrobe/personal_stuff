package com.example.personal_stuff.models

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val user: User?
)

data class CheckoutResponse(
    val success: Boolean,
    val message: String?,
    val orderId: Int?
)