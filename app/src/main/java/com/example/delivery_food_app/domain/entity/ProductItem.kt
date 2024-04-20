package com.example.delivery_food_app.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(
    var count: Int = 1,
    var price: Int = 0,
    val product: Product
) : Parcelable {

    val id: Long get() = product.id

}