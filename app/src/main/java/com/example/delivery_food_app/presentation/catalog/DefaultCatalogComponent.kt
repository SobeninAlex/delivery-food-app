package com.example.delivery_food_app.presentation.catalog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.utill.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultCatalogComponent @AssistedInject constructor(
    private val storeFactory: CatalogStoreFactory,
    @Assisted("onProductItemClicked") private val onProductItemClicked: (Product) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : CatalogComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is CatalogStore.Label.ClickProduct -> {
                        onProductItemClicked(it.product)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CatalogStore.State> = store.stateFlow

    override fun onClickProduct(product: Product) {
        store.accept(CatalogStore.Intent.ClickProduct(product))
    }

    override fun onClickAddToBasket(product: Product) {
        store.accept(CatalogStore.Intent.ClickAddToBasket(product))
    }

    override fun onClickRemoveFromBasket(product: Product) {
        store.accept(CatalogStore.Intent.ClickRemoveFromBasket(product))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onProductItemClicked") onProductItemClicked: (Product) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultCatalogComponent
    }
}