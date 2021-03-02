package com.example.restaurant.usecase

import com.example.restaurant.data.RestaurantWithCart
import com.example.restaurant.source.DataSource
import io.reactivex.Flowable

class QueryCart(private val dataSource: DataSource) {
    operator fun invoke() : Flowable<RestaurantWithCart> = dataSource.queryItemInCart()
}
