package com.example.delivery_food_app.data.network.dto

import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
