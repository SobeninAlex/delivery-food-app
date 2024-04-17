package com.example.delivery_food_app.presentation.catalog

import com.example.delivery_food_app.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface CatalogComponent {

    val model: StateFlow<CatalogStore.State>

    fun onClickProduct(product: Product)

    fun onClickAddToBasket(product: Product)

    fun onClickRemoveFromBasket(product: Product)

}