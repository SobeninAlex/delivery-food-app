package com.example.delivery_food_app.domain.repository

import com.example.delivery_food_app.domain.entity.ProductItem

interface SearchRepository {

    suspend fun search(query: String): List<ProductItem>

}