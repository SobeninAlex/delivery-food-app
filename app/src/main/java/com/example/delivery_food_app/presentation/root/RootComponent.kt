package com.example.delivery_food_app.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.delivery_food_app.presentation.basket.BasketComponent
import com.example.delivery_food_app.presentation.catalog.CatalogComponent
import com.example.delivery_food_app.presentation.details.DetailsComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Basket(val component: BasketComponent) : Child

        data class Catalog(val component: CatalogComponent) : Child

        data class Details(val component: DetailsComponent) : Child
    }

}