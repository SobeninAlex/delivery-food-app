package com.example.delivery_food_app.domain.repository

import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem

interface ProductCatalogRepository {

    suspend fun getProductCatalog(): List<ProductItem>

}