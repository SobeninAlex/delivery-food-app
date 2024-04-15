package com.example.delivery_food_app

import android.app.Application
import com.example.delivery_food_app.di.ApplicationComponent
import com.example.delivery_food_app.di.DaggerApplicationComponent

class DeliveryFoodApplication : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }

}