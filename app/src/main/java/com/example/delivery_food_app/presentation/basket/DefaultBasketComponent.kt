package com.example.delivery_food_app.presentation.basket

import com.arkivanov.decompose.ComponentContext
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

class DefaultBasketComponent @AssistedInject constructor(
    private val storeFactory: BasketStoreFactory,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : BasketComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is BasketStore.Label.ClickBack -> {
                        onBackClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<BasketStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(BasketStore.Intent.ClickBack)
    }

    override fun onClickAddToBasket(productItem: ProductItem) {
        store.accept(BasketStore.Intent.ClickAddToBasket(productItem))
    }

    override fun onClickRemoveFromBasket(productItem: ProductItem) {
        store.accept(BasketStore.Intent.ClickRemoveFromBasket(productItem))
    }

    override fun onSwipeToDeleteProductItem(productItem: ProductItem) {
        store.accept(BasketStore.Intent.SwipeToDeleteProductItem(productItem))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultBasketComponent
    }

}