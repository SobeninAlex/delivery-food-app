package com.example.delivery_food_app.di.module

import com.example.delivery_food_app.data.network.api.ApiFactory
import com.example.delivery_food_app.data.network.api.ApiService
import com.example.delivery_food_app.data.repository.BasketRepositoryImpl
import com.example.delivery_food_app.data.repository.ProductCatalogRepositoryImpl
import com.example.delivery_food_app.data.repository.ProductDetailRepositoryImpl
import com.example.delivery_food_app.data.repository.SearchRepositoryImpl
import com.example.delivery_food_app.di.annotation.ApplicationScope
import com.example.delivery_food_app.domain.repository.BasketRepository
import com.example.delivery_food_app.domain.repository.ProductCatalogRepository
import com.example.delivery_food_app.domain.repository.ProductDetailRepository
import com.example.delivery_food_app.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindBasketRepository(impl: BasketRepositoryImpl): BasketRepository

    @[ApplicationScope Binds]
    fun bindProductCatalogRepository(impl: ProductCatalogRepositoryImpl): ProductCatalogRepository

    @[ApplicationScope Binds]
    fun bindProductDetailRepositoryImpl(impl: ProductDetailRepositoryImpl): ProductDetailRepository

    @[ApplicationScope Binds]
    fun bindSearchRepositoryImpl(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

    }

}