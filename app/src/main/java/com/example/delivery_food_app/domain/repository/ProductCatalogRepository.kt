package com.example.delivery_food_app.domain.repository

import com.example.delivery_food_app.domain.entity.Product

interface ProductCatalogRepository {

    suspend fun getProductCatalog(): List<Product>

}