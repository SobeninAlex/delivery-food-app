package com.example.delivery_food_app.presentation.basket

import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import kotlinx.coroutines.flow.StateFlow

interface BasketComponent {

    val model: StateFlow<BasketStore.State>

    fun onClickBack()

    fun onClickAddToBasket(productItem: ProductItem)

    fun onClickRemoveFromBasket(productItem: ProductItem)

}