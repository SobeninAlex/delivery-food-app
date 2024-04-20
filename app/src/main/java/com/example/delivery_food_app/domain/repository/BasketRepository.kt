package com.example.delivery_food_app.domain.repository

import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductWithCount
import kotlinx.coroutines.flow.Flow

interface BasketRepository {

    val basketContent: Flow<Map<Product, ProductWithCount>>

    suspend fun addToBasket(product: Product)

    suspend fun removeFromBasket(product: Product)

}