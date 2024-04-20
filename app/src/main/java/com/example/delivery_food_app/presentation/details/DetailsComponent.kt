package com.example.delivery_food_app.presentation.details

import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    //модель экрана
    val model: StateFlow<DetailsStore.State>

    //действия пользователя
    fun onClickBack()

    fun onClickAddToBasket(productItem: ProductItem)

}