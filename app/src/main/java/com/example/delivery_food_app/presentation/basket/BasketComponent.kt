package com.example.delivery_food_app.presentation.basket

import com.example.delivery_food_app.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface BasketComponent {

    val model: StateFlow<BasketStore.State>

    fun onClickBack()

    fun onClickAddToBasket(product: Product)

    fun onClickRemoveFromBasket(product: Product)

}