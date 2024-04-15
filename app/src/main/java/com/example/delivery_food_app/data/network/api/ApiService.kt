package com.example.delivery_food_app.data.network.api

import com.example.delivery_food_app.data.network.dto.CategoryDto
import com.example.delivery_food_app.data.network.dto.ProductDto
import com.example.delivery_food_app.data.network.dto.TagDto
import retrofit2.http.GET

interface ApiService {

    @GET("Products.json")
    suspend fun getProducts(): List<ProductDto>

    @GET("Tags.json")
    suspend fun getTags(): List<TagDto>

    @GET("Categories.json")
    suspend fun getCategories(): List<CategoryDto>

}