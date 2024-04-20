package com.example.delivery_food_app.presentation.catalog

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.usecase.ChangeContentBasketUseCase
import com.example.delivery_food_app.domain.usecase.GetProductsCatalogUseCase
import com.example.delivery_food_app.presentation.catalog.CatalogStore.Intent
import com.example.delivery_food_app.presentation.catalog.CatalogStore.Label
import com.example.delivery_food_app.presentation.catalog.CatalogStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CatalogStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ClickProduct(val product: Product) : Intent

        data class ClickAddToBasket(val product: Product) : Intent

        data class ClickRemoveFromBasket(val product: Product) : Intent

        data object ClickBasketIcon : Intent
    }

    data class State(
        val productsStatus: ProductStatus
    ) {

        sealed interface ProductStatus {
            data object Initial : ProductStatus

            data object Loading : ProductStatus

            data object Error : ProductStatus

            data class Loaded(val products: List<Product>) : ProductStatus
        }
    }

    sealed interface Label {
        data class ClickProduct(val product: Product) : Label

        data object ClickBasketIcon : Label
    }

}

class CatalogStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val changeContentBasketUseCase: ChangeContentBasketUseCase,
    private val getProductsCatalogUseCase: GetProductsCatalogUseCase
) {

    fun create(): CatalogStore =
        object : CatalogStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CatalogStore",
            initialState = State(
                productsStatus = State.ProductStatus.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object CatalogStartLoading : Action

        data object CatalogLoadingError : Action

        data class CatalogLoaded(val products: List<Product>) : Action
    }

    private sealed interface Msg {
        data object CatalogStartLoading : Msg

        data object CatalogLoadingError : Msg

        data class CatalogLoaded(val products: List<Product>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.CatalogStartLoading)
                try {
                    val catalog = getProductsCatalogUseCase()
                    dispatch(Action.CatalogLoaded(catalog))
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
                        changeContentBasketUseCase.addToBasket(intent.product)
                    }
                }

                is Intent.ClickProduct -> {
                    publish(Label.ClickProduct(intent.product))
                }

                is Intent.ClickRemoveFromBasket -> {
                    scope.launch {
                        changeContentBasketUseCase.removeFromBasket(intent.product)
                    }
                }

                is Intent.ClickBasketIcon -> {
                    publish(Label.ClickBasketIcon)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.CatalogLoaded -> {
                    dispatch(Msg.CatalogLoaded(action.products))
                }

                is Action.CatalogLoadingError -> {
                    dispatch(Msg.CatalogLoadingError)
                }

                is Action.CatalogStartLoading -> {
                    dispatch(Msg.CatalogStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.CatalogLoaded -> {
                copy(
                    productsStatus = State.ProductStatus.Loaded(msg.products)
                )
            }

            is Msg.CatalogLoadingError -> {
                copy(
                    productsStatus = State.ProductStatus.Error
                )
            }

            is Msg.CatalogStartLoading -> {
                copy(
                    productsStatus = State.ProductStatus.Loading
                )
            }
        }
    }

}
