package com.example.delivery_food_app.presentation.search

import com.example.delivery_food_app.domain.entity.ProductItem
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun changeSearchQuery(searchQuery: String)

    fun onClickBack()

    fun onClickProduct(productItem: ProductItem)

    fun onClickAddToBasket(productItem: ProductItem)

    fun onClickRemoveFromBasket(productItem: ProductItem)

}