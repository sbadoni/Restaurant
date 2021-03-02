package com.example.restaurant.source

import android.app.Application
import com.example.restaurant.data.ItemInCart
import com.example.restaurant.data.Response
import com.example.restaurant.data.RestaurantWithCart
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.io.InputStreamReader

class DataSourceImpl(private val gSon: Gson,
                     private val context: Application): DataSource {

    private val itemInCartPublisher = BehaviorProcessor.createDefault(RestaurantWithCart.EMPTY)
    private val itemInCart = itemInCartPublisher.serialize()

    override fun getStaticData(): Flowable<Response> {
        val inputStream: InputStream = context.assets.open("Response.json")
        val reader = InputStreamReader(inputStream)
        val res = gSon.fromJson(reader, Response::class.java)
        println("res = $res")
        return  Flowable.just(res)
    }

    override fun getResponseFromCloud(): Flowable<Response> {
        TODO("Not yet implemented")
    }

    override fun getMustTry(): Flowable<Response> {
        TODO("Not yet implemented")
    }

    override fun addIntoTheCart(
        cuisineId: Int,
        foodId: Int,
        foodName: String,
        foodPrice: Int,
        quantity: Int
    )
        {
            itemInCartPublisher.value?.let {
                    val find = it.cartItems.find { itemInCart -> itemInCart.foodId == foodId }
                //println("itemInCartPublisher find ${find}")
                    find?.let {
                      //  println("itemInCartPublisher find ${find} 111")
                        it.quantity++
                    }?: kotlin.run {
                    //    println("itemInCartPublisher find ${find} 222")
                        it.cuisineId = cuisineId
                        it.cartItems.add(ItemInCart(cuisineId, foodId, foodName, foodPrice, quantity) )
                    }

            }
            println("addIntoTheCart ${itemInCartPublisher.value!!}")
            itemInCartPublisher.onNext(itemInCartPublisher.value!!)
        }


    override fun removeFromCart(cuisineId: Int, foodId: Int) {
            itemInCartPublisher.value?.let {
                val find = it.cartItems.find { it.foodId == foodId }
                find?.let {
                    it.quantity--
                }
                val isAnyAvailable = it.cartItems.any { it.quantity > 0 }
                if(isAnyAvailable.not()){
                    println("All items are zero clear the cart")
                    clearCart()
                    return@removeFromCart
                }
            }
            itemInCartPublisher.onNext(itemInCartPublisher.value!!)
        }

    override fun clearCart() {
        itemInCartPublisher.value?.let {
            it.cuisineId = RestaurantWithCart.EMPTY.cuisineId
            it.cartItems.clear()
            println("clearCart 1 ${itemInCartPublisher.value!!}")
        }
        println("clearCart ${itemInCartPublisher.value!!}")
        itemInCartPublisher.onNext(itemInCartPublisher.value!!)
    }

    override fun queryItemInCart(): Flowable<RestaurantWithCart> = itemInCart

}
