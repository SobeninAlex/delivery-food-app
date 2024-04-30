package com.example.delivery_food_app.presentation.catalog

import com.example.delivery_food_app.domain.entity.Category
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import kotlinx.coroutines.flow.StateFlow

interface CatalogComponent {

    val model: StateFlow<CatalogStore.State>

    fun onClickProduct(productItem: ProductItem)

    fun onClickAddToBasket(productItem: ProductItem)

    fun onClickRemoveFromBasket(productItem: ProductItem)

    fun onClickBasketIcon()

    fun onClickSearchIcon()

}