package com.example.restaurant.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurant.source.DataSource
import com.example.restaurant.source.DataSourceImpl
import com.example.restaurant.ui.CuisineAdapter
import com.example.restaurant.ui.MainViewModel
import com.example.restaurant.ui.MustTryAdapter
import com.example.restaurant.ui.OnCuisineClicked
import com.example.restaurant.ui.finalorder.FinalOrderAdapter
import com.example.restaurant.ui.finalorder.FinalOrderViewModel
import com.example.restaurant.ui.mainmenu.MenuAdapter
import com.example.restaurant.ui.mainmenu.MenuViewModel
import com.example.restaurant.usecase.*
import com.example.restaurant.util.UpdateFoodCounter
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RestaurantModule = module {

    single<Resources> { get<Application>().resources }
    single { Gson() }
    single<DataSource> { DataSourceImpl(get(), androidApplication()) }
    single { QueryCuisine(get()) }
    single { CommandAddToCart(get()) }
    single { CommandRemoveFromCart(get()) }
    single { CommandClearCart(get()) }
    single { QueryCart(get()) }
    //viewModels
    viewModel {
        val cuisineId: Int = it[0]
        MenuViewModel(get(), cuisineId, get(), get(), get(), get())
    }

    viewModel {
        MainViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        FinalOrderViewModel(get())
    }

    factory {
        val layoutInflater: LayoutInflater = it[0]
        val context: Context = it[1]
        val onCuisineClicked: OnCuisineClicked = it[2]
        CuisineAdapter(layoutInflater, get(), context, onCuisineClicked)
    }

    factory {
        val layoutInflater: LayoutInflater = it[0]
        val context: Context = it[1]
        val updateFoodCounter: UpdateFoodCounter = it[2]
        MustTryAdapter(layoutInflater, get(), context, updateFoodCounter)
    }

    factory {
        val layoutInflater: LayoutInflater = it[0]
        val context: Context = it[1]
        val updateFoodCounter: UpdateFoodCounter = it[2]
        MenuAdapter(layoutInflater, get(), context, updateFoodCounter)
    }


    factory {
        val layoutInflater: LayoutInflater = it[0]
        FinalOrderAdapter(layoutInflater, get())
    }

    //util
    factory { LinearLayoutManager(androidApplication()) }
}
