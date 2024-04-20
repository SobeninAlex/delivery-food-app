package com.example.delivery_food_app.domain.usecase

import com.example.delivery_food_app.domain.repository.SearchRepository
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(query: String) = repository.search(query = query)

}