package com.example.delivery_food_app.presentation.catalog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStop
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.utill.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultCatalogComponent @AssistedInject constructor(
    private val storeFactory: CatalogStoreFactory,
    @Assisted("onProductItemClicked") private val onProductItemClicked: (ProductItem) -> Unit,
    @Assisted("onBasketIconClicked") private val onBasketIconClicked: () -> Unit,
    @Assisted("onSearchIconClicked") private val onSearchIconClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : CatalogComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is CatalogStore.Label.ClickProduct -> {
                        onProductItemClicked(it.productItem)
                    }

                    is CatalogStore.Label.ClickBasketIcon -> {
                        onBasketIconClicked()
                    }

                    CatalogStore.Label.ClickSearchIcon -> {
                        onSearchIconClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CatalogStore.State> = store.stateFlow

    override fun onClickProduct(productItem: ProductItem) {
        store.accept(CatalogStore.Intent.ClickProduct(productItem))
    }

    override fun onClickAddToBasket(productItem: ProductItem) {
        store.accept(CatalogStore.Intent.ClickAddToBasket(productItem))
    }

    override fun onClickRemoveFromBasket(productItem: ProductItem) {
        store.accept(CatalogStore.Intent.ClickRemoveFromBasket(productItem))
    }

    override fun onClickBasketIcon() {
        store.accept(CatalogStore.Intent.ClickBasketIcon)
    }

    override fun onClickSearchIcon() {
        store.accept(CatalogStore.Intent.ClickSearchIcon)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onProductItemClicked") onProductItemClicked: (ProductItem) -> Unit,
            @Assisted("onBasketIconClicked") onBasketIconClicked: () -> Unit,
            @Assisted("onSearchIconClicked") onSearchIconClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultCatalogComponent
    }
}