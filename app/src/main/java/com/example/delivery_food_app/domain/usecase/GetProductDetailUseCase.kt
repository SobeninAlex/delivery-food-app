package com.example.delivery_food_app.domain.usecase

import com.example.delivery_food_app.domain.repository.ProductDetailRepository
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val repository: ProductDetailRepository
) {

    suspend operator fun invoke(productId: Long) =
        repository.getProduct(productId = productId)

}