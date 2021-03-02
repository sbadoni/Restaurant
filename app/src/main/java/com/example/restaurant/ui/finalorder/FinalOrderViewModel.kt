package com.example.restaurant.ui.finalorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurant.data.RestaurantWithCart
import com.example.restaurant.usecase.QueryCart
import io.reactivex.disposables.CompositeDisposable

class FinalOrderViewModel(private val queryCart: QueryCart): ViewModel() {
    private val cartLiveData = MutableLiveData<List<FoodItemWithPrice>>()
    private val disposable = CompositeDisposable()
    private val initialViewState = FinalOrderViewState.EMPTY
    init {
        disposable.add(
                queryCart()
                .subscribe(
                    { response -> mapCart(response)},
                    { throwable -> println("Error... $throwable") })
        )
    }

    private fun mapCart(restaurantWithCart: RestaurantWithCart){
        initialViewState.listOfFoodWithPrice.clear()
        initialViewState.listOfFoodWithPrice.apply {
            restaurantWithCart.cartItems.forEach {
                add(FoodItemWithPrice(it.foodId, it.foodName, it.foodPrice))
            }
        }
        cartLiveData.postValue(initialViewState.listOfFoodWithPrice)
    }

    fun observerCartData() = cartLiveData
}
