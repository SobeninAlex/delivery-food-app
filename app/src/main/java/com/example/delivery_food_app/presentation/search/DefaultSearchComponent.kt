package com.example.delivery_food_app.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.utill.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSearchComponent @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("onProductClicked") private val onProductClicked: (ProductItem) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is SearchStore.Label.ClickBack -> {
                        onBackClicked()
                    }
                    is SearchStore.Label.ClickProduct -> {
                        onProductClicked(label.productItem)
                    }
                }
            }
        }
    }

    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun changeSearchQuery(searchQuery: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(searchQuery))
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onClickSearch() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onClickProduct(productItem: ProductItem) {
        store.accept(SearchStore.Intent.ClickProduct(productItem))
    }

    override fun onClickAddToBasket(productItem: ProductItem) {
        store.accept(SearchStore.Intent.ClickAddToBasket(productItem))
    }

    override fun onClickRemoveFromBasket(productItem: ProductItem) {
        store.accept(SearchStore.Intent.ClickRemoveFromBasket(productItem))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onProductClicked") onProductClicked: (ProductItem) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultSearchComponent
    }

}