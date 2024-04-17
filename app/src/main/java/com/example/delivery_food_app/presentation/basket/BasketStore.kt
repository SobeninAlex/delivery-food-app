package com.example.delivery_food_app.presentation.basket

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.usecase.ChangeContentBasketUseCase
import com.example.delivery_food_app.domain.usecase.GetContentBasketUseCase
import com.example.delivery_food_app.presentation.basket.BasketStore.Intent
import com.example.delivery_food_app.presentation.basket.BasketStore.Label
import com.example.delivery_food_app.presentation.basket.BasketStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface BasketStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent

        data class ClickAddToBasket(val product: Product) : Intent

        data class ClickRemoveFromBasket(val product: Product) : Intent
    }

    data class State(
        val productState: ProductState
    ) {
        sealed interface ProductState {
            data object Initial : ProductState

            data object EmptyResult: ProductState

            data class Loaded(val products: List<Product>) : ProductState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
    }
}

class BasketStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getContentBasketUseCase: GetContentBasketUseCase,
    private val changeContentBasketUseCase: ChangeContentBasketUseCase,
) {

    fun create(): BasketStore =
        object : BasketStore, Store<Intent, State, Label> by storeFactory.create(
            name = "BasketStore",
            initialState = State(
                productState = State.ProductState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class BasketContentLoaded(val products: List<Product>) : Action
    }

    private sealed interface Msg {
        data class BasketContentLoaded(val products: List<Product>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getContentBasketUseCase().collect { products ->
                    dispatch(Action.BasketContentLoaded(products))
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

                is Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickRemoveFromBasket -> {
                    scope.launch {
                        changeContentBasketUseCase.removeFromBasket(intent.product)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.BasketContentLoaded -> {
                    dispatch(Msg.BasketContentLoaded(action.products))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.BasketContentLoaded -> {
                val state = if (msg.products.isEmpty()) {
                    State.ProductState.EmptyResult
                } else {
                    State.ProductState.Loaded(msg.products)
                }
                copy(
                    productState = state
                )
            }
        }
    }
}
