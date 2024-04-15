package com.example.delivery_food_app.data.mapper

import com.example.delivery_food_app.data.network.dto.ProductDto
import com.example.delivery_food_app.domain.entity.Product

fun ProductDto.toEntity(): Product = Product(
    id = this.id,
    categoryId = this.categoryId,
    name = this.name,
    description = this.description,
    priceCurrent = this.priceCurrent.toFormattedPrice(),
    priceOld = this.priceOld?.toFormattedPrice(),
    weight = "${this.measure} ${this.measureUnit}",
    energyValue = "${this.energyPer100Grams} ккал",
    proteins = this.proteinsPer100Grams.toString(),
    fats = this.fatsPer100Grams.toString(),
    carbohydrates = this.carbohydratesPer100Grams.toString()
)

private fun Long.toFormattedPrice(): String {
     return (this / 100).toInt().toString()
}