package com.example.delivery_food_app.data.repository

import android.util.Log
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.repository.BasketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BasketRepositoryImpl @Inject constructor() : BasketRepository {

    private val _basketList = mutableMapOf<Long, ProductItem>()
    private val basketList get() = _basketList.values.toList()

    private val basketChangeEvent = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override val basketContent = flow {
        basketChangeEvent.collect {
            emit(basketList)
            Log.d("TAG", "basketChangeEvent")
        }
    }

    override suspend fun addToBasket(productItem: ProductItem) {
        val item = basketList.find {
            it.id == productItem.id
        }

        if (item != null) {
            val newCount = item.count + 1
            val newPrice = item.price + productItem.product.priceCurrent
            val newProductItem = item.copy(
                count = newCount,
                price = newPrice
            )
            _basketList[item.id] = newProductItem
            Log.d("Basket", "edit product $newProductItem")
        } else {
            val newProduct = ProductItem(
                count = 1,
                price = productItem.product.priceCurrent,
                product = productItem.product
            )
            _basketList[productItem.id] = newProduct
            Log.d("Basket", "add new product $newProduct")
        }

        basketChangeEvent.tryEmit(Unit)
        Log.d("Basket", "emit basketList")
    }

    override suspend fun removeFromBasket(productItem: ProductItem) {
        val item = basketList.find {
            it.id == productItem.id
        }

        if (item != null && item.count > 1) {
            val newCount = item.count - 1
            val newPrice = item.price - productItem.product.priceCurrent
            val newProductItem = item.copy(
                count = newCount,
                price = newPrice
            )
            _basketList[item.id] = newProductItem
            Log.d("Basket", "edit product $newProductItem")
        } else {
            _basketList.remove(productItem.id)
            Log.d("Basket", "remove product")
        }

        basketChangeEvent.tryEmit(Unit)
        Log.d("Basket", "emit basketList")
    }

}