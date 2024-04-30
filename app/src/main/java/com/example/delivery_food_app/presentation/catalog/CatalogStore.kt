package com.example.delivery_food_app.presentation.catalog

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.delivery_food_app.domain.entity.Category
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.usecase.GetProductsCatalogUseCase
import com.example.delivery_food_app.presentation.catalog.CatalogStore.Intent
import com.example.delivery_food_app.presentation.catalog.CatalogStore.Label
import com.example.delivery_food_app.presentation.catalog.CatalogStore.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CatalogStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ClickProduct(val productItem: ProductItem) : Intent

        data class ClickAddToBasket(val productItem: ProductItem) : Intent

        data class ClickRemoveFromBasket(val productItem: ProductItem) : Intent

        data object ClickBasketIcon : Intent

        data object ClickSearchIcon : Intent
    }

    data class State(
        val categories: List<Category>,
        val catalogStatus: CatalogStatus
    ) {

        sealed interface CatalogStatus {
            data object Initial : CatalogStatus

            data object Loading : CatalogStatus

            data object Error : CatalogStatus

            data class Loaded(val catalog: List<ProductItem>) : CatalogStatus
        }
    }

    sealed interface Label {
        data class ClickProduct(val productItem: ProductItem) : Label

        data object ClickBasketIcon : Label

        data object ClickSearchIcon : Label
    }

}

class CatalogStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getProductsCatalogUseCase: GetProductsCatalogUseCase
) {

    fun create(): CatalogStore =
        object : CatalogStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CatalogStore",
            initialState = State(
                categories = emptyList(),
                catalogStatus = State.CatalogStatus.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object CatalogStartLoading : Action

        data object CatalogLoadingError : Action

        data class CatalogLoaded(val catalog: List<ProductItem>) : Action

        data class CategoryLoaded(val categories: List<Category>) : Action
    }

    private sealed interface Msg {
        data object CatalogStartLoading : Msg

        data object CatalogLoadingError : Msg

        data class CatalogLoaded(val catalog: List<ProductItem>) : Msg

        data class CategoryLoaded(val categories: List<Category>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.CatalogStartLoading)
                try {
                    getProductsCatalogUseCase.getCategories().collect {
                        dispatch(Action.CategoryLoaded(it))
                    }
                    getProductsCatalogUseCase.getProductCatalog().collect {
                        dispatch(Action.CatalogLoaded(it))
                    }
                } catch (e: Exception) {
                    dispatch(Action.CatalogLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ClickAddToBasket -> {
                    scope.launch {
                        getProductsCatalogUseCase.addToBasket(intent.productItem)
                    }
                }

                is Intent.ClickProduct -> {
                    publish(Label.ClickProduct(intent.productItem))
                }

                is Intent.ClickRemoveFromBasket -> {
                    scope.launch {
                        getProductsCatalogUseCase.removeFromBasket(intent.productItem)
                    }
                }

                is Intent.ClickBasketIcon -> {
                    publish(Label.ClickBasketIcon)
                }

                Intent.ClickSearchIcon -> {
                    publish(Label.ClickSearchIcon)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.CatalogLoaded -> {
                    dispatch(Msg.CatalogLoaded(action.catalog))
                }

                is Action.CatalogLoadingError -> {
                    dispatch(Msg.CatalogLoadingError)
                }

                is Action.CatalogStartLoading -> {
                    dispatch(Msg.CatalogStartLoading)
                }

                is Action.CategoryLoaded -> {
                    dispatch(Msg.CategoryLoaded(action.categories))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.CatalogLoaded -> {
                copy(
                    catalogStatus = State.CatalogStatus.Loaded(msg.catalog)
                )
            }

            is Msg.CatalogLoadingError -> {
                copy(
                    catalogStatus = State.CatalogStatus.Error
                )
            }

            is Msg.CatalogStartLoading -> {
                copy(
                    catalogStatus = State.CatalogStatus.Loading
                )
            }

            is Msg.CategoryLoaded -> {
                copy(
                    categories = msg.categories
                )
            }
        }
    }

}
