package com.example.delivery_food_app.data.local

import com.example.delivery_food_app.domain.entity.ProductItem

object ProductCatalog {

    private val _catalog = mutableMapOf<Long, ProductItem>()

    fun addToCatalog(productItem: ProductItem) {
        _catalog[productItem.id] = productItem
    }

    fun getProductItemFormCatalog(productId: Long): ProductItem {
        return _catalog[productId] ?: throw RuntimeException("item not found")
    }

}