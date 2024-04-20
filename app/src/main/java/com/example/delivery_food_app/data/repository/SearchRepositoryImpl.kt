package com.example.delivery_food_app.data.repository

import com.example.delivery_food_app.data.local.ProductCatalog
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    override suspend fun search(query: String): List<ProductItem> {
        return ProductCatalog.searchProduct(query)
    }

}