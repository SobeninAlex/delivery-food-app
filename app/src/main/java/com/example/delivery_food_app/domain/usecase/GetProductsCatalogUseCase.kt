package com.example.delivery_food_app.domain.usecase

import com.example.delivery_food_app.domain.repository.ProductCatalogRepository
import javax.inject.Inject

class GetProductsCatalogUseCase @Inject constructor(
    private val repository: ProductCatalogRepository
) {

    suspend operator fun invoke() = repository.getProductCatalog()

}