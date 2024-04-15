package com.example.delivery_food_app.di

import android.content.Context
import com.example.delivery_food_app.di.annotation.ApplicationScope
import com.example.delivery_food_app.di.module.DataModule
import com.example.delivery_food_app.di.module.PresentationModule
import com.example.delivery_food_app.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        PresentationModule::class,
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }

}