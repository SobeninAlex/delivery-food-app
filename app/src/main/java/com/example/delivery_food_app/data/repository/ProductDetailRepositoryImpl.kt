package com.example.delivery_food_app.data.repository

import android.content.res.Resources.NotFoundException
import com.example.delivery_food_app.data.local.ProductCatalog
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.repository.ProductDetailRepository
import javax.inject.Inject

class ProductDetailRepositoryImpl @Inject constructor() : ProductDetailRepository {

    override suspend fun getProduct(productId: Long): Product? {
        return ProductCatalog.catalog[productId]
    }

}