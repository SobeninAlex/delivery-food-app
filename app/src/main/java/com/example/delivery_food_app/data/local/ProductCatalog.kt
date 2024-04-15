package com.example.delivery_food_app.data.local

import com.example.delivery_food_app.domain.entity.Product
import javax.inject.Inject

object ProductCatalog {

    private val _catalog = mutableMapOf<Long, Product>()
    val catalog get() = _catalog.toMap()

    fun addToCatalog(product: Product) {
        _catalog[product.id] = product
    }

}