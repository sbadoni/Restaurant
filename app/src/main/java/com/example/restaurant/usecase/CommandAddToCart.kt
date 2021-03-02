package com.example.restaurant.usecase

import com.example.restaurant.data.Response
import com.example.restaurant.source.DataSource
import io.reactivex.Completable
import io.reactivex.Flowable

class CommandAddToCart (private val dataSource: DataSource) {
    operator fun invoke(request: Request)  = dataSource.addIntoTheCart(request.cuisineId, request.foodId, request.foodName, request.foodPrice, request.quantity)

    data class Request(val cuisineId: Int, val foodId: Int, val foodName: String, val foodPrice: Int, val quantity: Int)
}
