package com.example.delivery_food_app.domain.entity

data class Product(
    val id: Long,
    val categoryId: Long, //категория продукта
    val name: String, //название продукта
    val description: String, //описание продукта
    val priceCurrent: String, //текущая цена, ₽
    val priceOld: String?, //старая цена, если не null то отображается скидка, ₽
    val weight:String, //вес, г
    val energyValue: String, //энергетическая ценность, ккал
    val proteins: String, //белки, г
    val fats: String, //жиры, г
    val carbohydrates: String, //углеводы, г
//    val isSpicy: Boolean,
//    val isVegan: Boolean,
)

