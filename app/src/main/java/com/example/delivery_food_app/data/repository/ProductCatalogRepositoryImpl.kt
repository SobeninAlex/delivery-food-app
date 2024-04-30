package com.example.delivery_food_app.data.repository

import com.example.delivery_food_app.data.local.ProductCatalog
import com.example.delivery_food_app.data.mapper.toEntity
import com.example.delivery_food_app.data.network.api.ApiService
import com.example.delivery_food_app.domain.entity.Category
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.repository.BasketRepository
import com.example.delivery_food_app.domain.repository.ProductCatalogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class ProductCatalogRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val basketRepository: BasketRepository
) : ProductCatalogRepository {

    private val basketContentChangeEvent = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override val productCatalog: Flow<List<ProductItem>> = flow {
        val productList = apiService.getProducts().map { it.toEntity() }.map {
            ProductItem(
                product = it
            )
        }
        productList.forEach { productItem ->
            ProductCatalog.addToCatalog(
                productItem = productItem
            )
        }

        basketContentChangeEvent.collect {
            ProductCatalog.getAllCatalog().collect {
                emit(it)
            }
        }
    }

    override val categories: Flow<List<Category>> = flow {
        val categories = apiService.getCategories().map {
            Category(
                id = it.id,
                name = it.name
            )
        }
        emit(categories)
    }

    override suspend fun addToBasket(productItem: ProductItem) {
        basketRepository.addToBasket(productItem)
        val newProduct = productItem.copy(
            count = productItem.count + 1,
            price = productItem.price + productItem.product.priceCurrent
        )
        ProductCatalog.addToCatalog(newProduct)
        basketContentChangeEvent.tryEmit(Unit)
    }

    override suspend fun removeFromBasket(productItem: ProductItem) {
        basketRepository.removeFromBasket(productItem)
        val newProduct = productItem.copy(
            count = productItem.count - 1,
            price = productItem.price - productItem.product.priceCurrent
        )
        ProductCatalog.addToCatalog(newProduct)
        basketContentChangeEvent.tryEmit(Unit)
    }

}