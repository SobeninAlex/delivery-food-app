package com.example.delivery_food_app.data.repository

import android.util.Log
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.repository.BasketRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BasketRepositoryImpl @Inject constructor() : BasketRepository {

    private val _basketList = mutableListOf<ProductItem>()
    private val basketList get() = _basketList.toList()

    private val basketChangeEvent = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override val basketContent = flow {
        basketChangeEvent.collect {
            emit(basketList)
            Log.d("TAG", "basketChangeEvent")
        }
    }

    override suspend fun addToBasket(product: Product) {
        val productItem = basketList.find {
            it.product == product
        }

        if (productItem != null) {
            productItem.count++
            productItem.price += product.priceCurrent
            Log.d("Basket", "counter++")
        } else {
            val newProduct = ProductItem(
                id = product.id,
                count = 1,
                price = product.priceCurrent,
                product = product
            )
            _basketList.add(newProduct)
            Log.d("Basket", "add new product $newProduct")
        }

        basketChangeEvent.tryEmit(Unit)
        Log.d("Basket", "addToBasket")
    }

    override suspend fun removeFromBasket(product: Product) {
        val productItem = basketList.find {
            it.product == product
        }

        if (productItem != null && productItem.count > 1) {
            productItem.count--
            productItem.price -= product.priceCurrent
            Log.d("Basket", "counter--")
        } else {
            _basketList.remove(productItem)
            Log.d("Basket", "remover all products")
        }

        basketChangeEvent.tryEmit(Unit)
        Log.d("Basket", "removeFromBasket")
    }

}