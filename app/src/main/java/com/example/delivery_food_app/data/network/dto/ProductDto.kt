package com.example.delivery_food_app.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id") val id: Long,
    @SerializedName("category_id") val categoryId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price_current") val priceCurrent: Long,
    @SerializedName("price_old") val priceOld: Long?,
    @SerializedName("measure") val measure: Long,
    @SerializedName("measure_unit") val measureUnit: String,
    @SerializedName("energy_per_100_grams") val energyPer100Grams: Double,
    @SerializedName("proteins_per_100_grams") val proteinsPer100Grams: Double,
    @SerializedName("fats_per_100_grams") val fatsPer100Grams: Double,
    @SerializedName("carbohydrates_per_100_grams") val carbohydratesPer100Grams: Double,
    @SerializedName("tag_ids") val tagIds: List<Int>,
)
