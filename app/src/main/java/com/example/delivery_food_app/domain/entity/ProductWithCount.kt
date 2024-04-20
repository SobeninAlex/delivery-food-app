package com.example.delivery_food_app.domain.entity


data class ProductWithCount(
    val item: Product,
    var count: Int = 1,
    var price: Int = item.priceCurrent
)