package com.example.delivery_food_app.domain.usecase

import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.repository.BasketRepository
import javax.inject.Inject

class ChangeContentBasketUseCase @Inject constructor(
    private val repository: BasketRepository
) {

    suspend fun addToBasket(productItem: ProductItem) =
        repository.addToBasket(productItem = productItem)

    suspend fun removeFromBasket(productItem: ProductItem) =
        repository.removeFromBasket(productItem = productItem)

    suspend fun deleteProductItem(productItem: ProductItem) =
        repository.deleteItemProduct(productItem)

}