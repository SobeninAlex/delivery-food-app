package com.example.delivery_food_app.domain.repository

import com.example.delivery_food_app.domain.entity.ProductItem
import kotlinx.coroutines.flow.Flow

interface ProductCatalogRepository {

    val productCatalog: Flow<List<ProductItem>>

    suspend fun addToBasket(productItem: ProductItem)

    suspend fun removeFromBasket(productItem: ProductItem)

}