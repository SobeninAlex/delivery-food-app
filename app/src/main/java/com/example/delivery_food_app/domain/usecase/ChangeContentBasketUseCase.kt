package com.example.delivery_food_app.domain.usecase

import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.repository.BasketRepository
import javax.inject.Inject

class ChangeContentBasketUseCase @Inject constructor(
    private val repository: BasketRepository
) {

    suspend fun addToBasket(product: Product) =
        repository.addToBasket(product = product)

    suspend fun removeFromBasket(product: Product) =
        repository.removeFromBasket(product = product)

}