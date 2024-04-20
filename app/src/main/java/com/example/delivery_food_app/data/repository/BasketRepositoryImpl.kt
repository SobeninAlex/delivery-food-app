package com.example.delivery_food_app.data.repository

import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductWithCount
import com.example.delivery_food_app.domain.repository.BasketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BasketRepositoryImpl @Inject constructor() : BasketRepository {

    private val _basketList = mutableMapOf<Product, ProductWithCount>()
    private val basketList get() = _basketList.toMap()

    private val basketChangeEvent = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override val basketContent = flow {
        basketChangeEvent.collect {
            emit(basketList)
        }
    }

    override suspend fun addToBasket(product: Product) {
        val existingItem = basketList[product]
        if (existingItem != null) {
            existingItem.count++
            existingItem.price += existingItem.item.priceCurrent
        } else {
            _basketList[product] = ProductWithCount(product)
        }
        basketChangeEvent.tryEmit(Unit)
    }

    override suspend fun removeFromBasket(product: Product) {
        val existingItem = basketList[product]
        if (existingItem != null) {
            if (existingItem.count > 1) {
                existingItem.count--
                existingItem.price -= existingItem.item.priceCurrent
            } else {
                _basketList.remove(product)
            }
            basketChangeEvent.tryEmit(Unit)
        }
    }
}