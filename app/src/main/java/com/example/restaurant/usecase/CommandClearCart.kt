package com.example.restaurant.usecase

import com.example.restaurant.source.DataSource
import io.reactivex.Completable

class CommandClearCart(private val dataSource: DataSource) {
    operator fun invoke()  = dataSource.clearCart()
}
