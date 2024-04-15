package com.example.delivery_food_app.domain.usecase

import com.example.delivery_food_app.domain.repository.BasketRepository
import javax.inject.Inject

class GetContentBasketUseCase @Inject constructor(
    private val repository: BasketRepository
) {

    operator fun invoke() = repository.basketContent

}