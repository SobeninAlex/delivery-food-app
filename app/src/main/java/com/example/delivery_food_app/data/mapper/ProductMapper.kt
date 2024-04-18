package com.example.delivery_food_app.data.mapper

import com.example.delivery_food_app.data.network.dto.ProductDto
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.Tag

fun ProductDto.toEntity(): Product = Product(
    id = this.id,
    categoryId = this.categoryId,
    name = this.name,
    description = this.description,
    priceCurrent = this.priceCurrent.toFormattedPrice(),
    priceOld = this.priceOld?.toFormattedPrice(),
    weight = "${this.measure} ${this.measureUnit}",
    energyValue = "${this.energyPer100Grams} ккал",
    proteins = "${this.proteinsPer100Grams} ${this.measureUnit}",
    fats = "${this.fatsPer100Grams} ${this.measureUnit}",
    carbohydrates = "${this.carbohydratesPer100Grams} ${this.measureUnit}",
    tags = this.tagIds
)

private fun Long.toFormattedPrice(): String {
     return "${(this / 100).toInt()} ₽"
}