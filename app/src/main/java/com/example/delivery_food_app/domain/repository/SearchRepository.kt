package com.example.delivery_food_app.domain.repository

import com.example.delivery_food_app.domain.entity.ProductItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun search(query: String): Flow<List<ProductItem>>

}