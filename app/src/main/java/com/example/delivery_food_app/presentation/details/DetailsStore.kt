package com.example.delivery_food_app.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.usecase.ChangeContentBasketUseCase
import com.example.delivery_food_app.domain.usecase.GetProductDetailUseCase
import com.example.delivery_food_app.presentation.details.DetailsStore.Intent
import com.example.delivery_food_app.presentation.details.DetailsStore.Label
import com.example.delivery_food_app.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        //действия пользователя
        data object ClickBack : Intent

        data class ClickAddToBasket(val product: Product): Intent
    }

    data class State(
        val productState: ProductState
    ) {
        sealed interface ProductState {
            data object Initial : ProductState

            data object Loading : ProductState

            data object Error : ProductState

            data class Loaded(val product: Product?) : ProductState
        }
    }

    sealed interface Label {
        //действия для навигации
        data object ClickBack : Label
    }

}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val changeContentBasketUseCase: ChangeContentBasketUseCase
) {

    fun create(product: Product?): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                productState = State.ProductState.Initial
            ),
            bootstrapper = BootstrapperImpl(product),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        //загрузка из репозитория при создании DetailsStore
        data object ProductStartLoading : Action

        data object ProductLoadingError : Action

        data class ProductLoaded(val product: Product?) : Action
    }

    private sealed interface Msg {
        //действия при которых меняется стейт экрана
        data object ProductStartLoading : Msg

        data object ProductLoadingError : Msg

        data class ProductLoaded(val product: Product?) : Msg
    }

    private inner class BootstrapperImpl(private val product: Product?) : CoroutineBootstrapper<Action>() {

        //загрузка из репозитория при создании DetailsStore
        override fun invoke() {
            scope.launch {
                dispatch(Action.ProductStartLoading)
                try {
                    val product = product?.id?.let { getProductDetailUseCase(it) }
                    dispatch(Action.ProductLoaded(product))
                } catch (e: Exception) {
                    dispatch(Action.ProductLoadingError)
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
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.ProductLoaded -> {
                    dispatch(Msg.ProductLoaded(action.product))
                }

                is Action.ProductLoadingError -> {
                    dispatch(Msg.ProductLoadingError)
                }

                is Action.ProductStartLoading -> {
                    dispatch(Msg.ProductStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        //обработка сообщений, изменение стейта
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ProductLoaded -> {
                copy(
                    productState = State.ProductState.Loaded(msg.product)
                )
            }

            is Msg.ProductLoadingError -> {
                copy(
                    productState = State.ProductState.Error
                )
            }

            is Msg.ProductStartLoading -> {
                copy(
                    productState = State.ProductState.Loading
                )
            }
        }
    }

}
