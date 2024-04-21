package com.example.delivery_food_app.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.delivery_food_app.presentation.basket.BasketContent
import com.example.delivery_food_app.presentation.catalog.CatalogContent
import com.example.delivery_food_app.presentation.details.DetailsContent
import com.example.delivery_food_app.presentation.search.SearchContent
import com.example.delivery_food_app.presentation.ui.theme.DeliveryfoodappTheme

@Composable
fun RootContent(
    component: RootComponent
) {
    DeliveryfoodappTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Children(stack = component.stack) {
                when (val instance = it.instance) {
                    is RootComponent.Child.Basket -> {
                        BasketContent(component = instance.component)
                    }

                    is RootComponent.Child.Catalog -> {
                        CatalogContent(component = instance.component)
                    }

                    is RootComponent.Child.Details -> {
                        DetailsContent(component = instance.component)
                    }

                    is RootComponent.Child.Search -> {
                        SearchContent(component = instance.component)
                    }
                }
            }
        }
    }
}