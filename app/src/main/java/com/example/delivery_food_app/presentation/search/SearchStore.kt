package com.example.delivery_food_app.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.usecase.ChangeContentBasketUseCase
import com.example.delivery_food_app.domain.usecase.SearchProductUseCase
import com.example.delivery_food_app.presentation.search.SearchStore.Intent
import com.example.delivery_food_app.presentation.search.SearchStore.Label
import com.example.delivery_food_app.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeSearchQuery(val searchQuery: String) : Intent

        data object ClickBack : Intent

        data object ClickSearch : Intent

        data class ClickProduct(val productItem: ProductItem) : Intent

        data class ClickAddToBasket(val productItem: ProductItem) : Intent

        data class ClickRemoveFromBasket(val productItem: ProductItem) : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {
        sealed interface SearchState {
            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object EmptyResult : SearchState

            data class SuccessLoaded(val products: List<ProductItem>) : SearchState
        }
    }

    sealed interface Label {
        data object ClickBack : Label

        data class ClickProduct(val productItem: ProductItem) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val changeContentBasketUseCase: ChangeContentBasketUseCase,
    private val searchProductUseCase: SearchProductUseCase
) {

    fun create(): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {}

    private sealed interface Msg {
        data class ChangeSearchQuery(val searchQuery: String) : Msg

        data object Loading : Msg

        data object Error : Msg

        data class SuccessLoaded(val products: List<ProductItem>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {}
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.searchQuery))
                }

                is Intent.ClickAddToBasket -> {
                    scope.launch {
                        changeContentBasketUseCase.addToBasket(intent.productItem)
                    }
                }

                is Intent.ClickRemoveFromBasket -> {
                    scope.launch {
                        changeContentBasketUseCase.removeFromBasket(intent.productItem)
                    }
                }

                is Intent.ClickSearch -> {
                    searchJob?.cancel()

                    searchJob = scope.launch {
                        dispatch(Msg.Loading)
                        val searchQuery = getState().searchQuery
                        try {
                            if (searchQuery.isNotBlank()) {
                                val products = searchProductUseCase(searchQuery)
                                dispatch(Msg.SuccessLoaded(products = products))
                            }
                        } catch (e: Exception) {
                            dispatch(Msg.Error)
                        }
                    }
                }

                is Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickProduct -> {
                    publish(Label.ClickProduct(intent.productItem))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {}
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeSearchQuery -> {
                copy(
                    searchQuery = msg.searchQuery
                )
            }

            is Msg.Error -> {
                copy(
                    searchState = State.SearchState.Error
                )
            }

            is Msg.Loading -> {
                copy(
                    searchState = State.SearchState.Loading
                )
            }

            is Msg.SuccessLoaded -> {
                val searchState = if (msg.products.isEmpty()) {
                    State.SearchState.EmptyResult
                } else {
                    State.SearchState.SuccessLoaded(msg.products)
                }

                copy(searchState = searchState)
            }
        }
    }

}