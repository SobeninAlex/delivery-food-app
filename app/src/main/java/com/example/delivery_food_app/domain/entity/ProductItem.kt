package com.example.delivery_food_app.domain.entity

data class ProductItem(
    val id: Long,
    var count: Int,
    var price: Int,
    val product: Product
)