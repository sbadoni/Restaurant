package com.example.restaurant.usecase

import com.example.restaurant.source.DataSource
import io.reactivex.Completable

class CommandRemoveFromCart (private val dataSource: DataSource) {
    operator fun invoke(request: Request) = dataSource.removeFromCart(request.cuisineId, request.foodId)

    data class Request(val cuisineId: Int, val foodId: Int)
}
