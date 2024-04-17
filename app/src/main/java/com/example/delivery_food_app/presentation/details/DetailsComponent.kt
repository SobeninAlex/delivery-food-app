package com.example.delivery_food_app.presentation.details

import com.example.delivery_food_app.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    //модель экрана
    val model: StateFlow<DetailsStore.State>

    //действия пользователя
    fun onClickBack()

    fun onClickAddToBasket(product: Product)

}