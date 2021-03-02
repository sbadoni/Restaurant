package com.example.restaurant

import android.app.Application
import com.example.restaurant.di.RestaurantModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RestaurantApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RestaurantApplication)
            modules(RestaurantModule)
        }
    }
}
