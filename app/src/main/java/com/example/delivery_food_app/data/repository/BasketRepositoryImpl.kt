package com.example.delivery_food_app.data.repository

import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.repository.BasketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BasketRepositoryImpl @Inject constructor() : BasketRepository {

    private val _basketList = mutableListOf<Product>()
    private val basketList get() = _basketList.toList()

    private val basketChangeEvent = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override val basketContent: Flow<List<Product>> = flow {
        basketChangeEvent.collect {
            emit(basketList)
        }
    }

    override suspend fun addToBasket(product: Product) {
        _basketList.add(product)
        basketChangeEvent.tryEmit(Unit)
    }

    override suspend fun removeFromBasket(product: Product) {
        _basketList.remove(product)
        basketChangeEvent.tryEmit(Unit)
    }
}