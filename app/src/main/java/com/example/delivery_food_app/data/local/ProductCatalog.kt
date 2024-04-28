package com.example.delivery_food_app.data.local

import com.example.delivery_food_app.domain.entity.ProductItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

object ProductCatalog {

    private val _catalog = mutableMapOf<Long, ProductItem>()

    private val catalogChangeEvent = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    fun addToCatalog(productItem: ProductItem) {
        _catalog[productItem.id] = productItem
        catalogChangeEvent.tryEmit(Unit)
    }

    fun getProductItemFormCatalog(productId: Long): ProductItem {
        return _catalog[productId] ?: throw RuntimeException("item not found")
    }

    fun searchProduct(query: String): Flow<List<ProductItem>> = flow {
        val catalog = _catalog.values.filter {
            it.product.name.contains(query, ignoreCase = true)
        }
        catalogChangeEvent.collect {
            emit(catalog)
        }
    }

    fun getAllCatalog() = flow {
        catalogChangeEvent.collect {
            emit(_catalog.values.toList())
        }
    }

}