package com.example.delivery_food_app.domain.repository

import com.example.delivery_food_app.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface BasketRepository {

    val basketContent: Flow<List<Product>>

    suspend fun addToBasket(product: Product)

    suspend fun removeFromBasket(product: Product)

}