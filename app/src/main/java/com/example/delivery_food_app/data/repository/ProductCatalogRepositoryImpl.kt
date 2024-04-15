package com.example.delivery_food_app.data.repository

import com.example.delivery_food_app.data.local.ProductCatalog
import com.example.delivery_food_app.data.mapper.toEntity
import com.example.delivery_food_app.data.network.api.ApiService
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.repository.ProductCatalogRepository
import javax.inject.Inject

class ProductCatalogRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ProductCatalogRepository {

    override suspend fun getProductCatalog(): List<Product> {
        val response = apiService.getProducts().map { it.toEntity() }
        response.forEach { product ->
            ProductCatalog.addToCatalog(
                product = product
            )
        }
        return response
    }

}