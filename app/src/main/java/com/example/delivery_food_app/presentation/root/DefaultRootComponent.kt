package com.example.delivery_food_app.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.presentation.basket.DefaultBasketComponent
import com.example.delivery_food_app.presentation.catalog.DefaultCatalogComponent
import com.example.delivery_food_app.presentation.details.DefaultDetailsComponent
import com.example.delivery_food_app.presentation.search.DefaultSearchComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val basketComponentFactory: DefaultBasketComponent.Factory,
    private val catalogComponentFactory: DefaultCatalogComponent.Factory,
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Catalog,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.Basket -> {
                val component = basketComponentFactory.create(
                    onBackClicked = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Basket(component)
            }

            is Config.Catalog -> {
                val component = catalogComponentFactory.create(
                    onProductItemClicked = {
                        navigation.push(Config.Details(productItem = it))
                    },
                    onBasketIconClicked = {
                        navigation.push(Config.Basket)
                    },
                    onSearchIconClicked = {
                        navigation.push(Config.Search)
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Catalog(component)
            }

            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    productItem = config.productItem,
                    onBackClicked = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Details(component)
            }

            Config.Search -> {
                val component = searchComponentFactory.create(
                    onBackClicked = {
                        navigation.pop()
                    },
                    onProductClicked = {
                        navigation.push(Config.Details(productItem = it))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Search(component)
            }
        }
    }

    private sealed interface Config : Parcelable {

        //в парамеры передаю то что необходимо в StoreFactory.create()

        @Parcelize
        data object Basket : Config

        @Parcelize
        data object Catalog : Config

        @Parcelize
        data class Details(val productItem: ProductItem) : Config

        @Parcelize
        data object Search : Config

    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }

}