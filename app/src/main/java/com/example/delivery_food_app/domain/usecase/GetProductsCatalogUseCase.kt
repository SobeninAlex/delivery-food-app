package com.example.delivery_food_app.domain.usecase

import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.repository.ProductCatalogRepository
import javax.inject.Inject

class GetProductsCatalogUseCase @Inject constructor(
    private val repository: ProductCatalogRepository
) {

    fun getProductCatalog() = repository.productCatalog

    suspend fun addToBasket(productItem: ProductItem) = repository.addToBasket(productItem)

    suspend fun removeFromBasket(productItem: ProductItem) = repository.removeFromBasket(productItem)

}