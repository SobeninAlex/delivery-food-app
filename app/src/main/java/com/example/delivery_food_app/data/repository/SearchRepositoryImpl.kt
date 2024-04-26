package com.example.delivery_food_app.data.repository

import com.example.delivery_food_app.data.local.ProductCatalog
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    private val basketContentChangeEvent = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override fun search(query: String): Flow<List<ProductItem>> = flow {
        basketContentChangeEvent.collect {
            ProductCatalog.getAllCatalog().collect {
                emit(it)
            }
        }
    }



}