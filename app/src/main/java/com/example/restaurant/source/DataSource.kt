package com.example.restaurant.source

import com.example.restaurant.data.Response
import com.example.restaurant.data.RestaurantWithCart
import io.reactivex.Completable
import io.reactivex.Flowable

interface DataSource {

    fun getStaticData(): Flowable<Response>

    fun getResponseFromCloud(): Flowable<Response> // TODO ONCE WE MOVE TO CLOUD WE JUST NEED TO SWITCH THIS INTERFACE

    fun getMustTry(): Flowable<Response>

    fun addIntoTheCart(cuisineId: Int, foodId: Int, foodName: String, foodPrice: Int, quantity: Int)

    fun removeFromCart(cuisineId: Int, foodId: Int)

    fun clearCart()

    fun queryItemInCart(): Flowable<RestaurantWithCart>
}
