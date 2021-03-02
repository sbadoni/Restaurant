package com.example.restaurant.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurant.data.*
import com.example.restaurant.usecase.*
import com.example.restaurant.util.NotificationEvent
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction

class MainViewModel(
    private val queryCuisine: QueryCuisine,
    private val queryCart: QueryCart,
    private val commandAddToCart: CommandAddToCart,
    private val clearCart: CommandClearCart,
    private val commandRemoveFromCart: CommandRemoveFromCart
) : ViewModel() {

    private val cuisinesLiveData = MutableLiveData<List<CuisineItem>>()
    private val mustTryLiveData = MutableLiveData<List<MustTryItem>>()
    private val cartLiveData = MutableLiveData<Int>()
    private val cartClearLiveData = MutableLiveData<NotificationEvent<Unit>>()
    private val disposable = CompositeDisposable()
    private val initialViewState = MainViewState.EMPTY
    private var currentSelectedCuisine: Int? = null
    private var checkCartDisposable = Disposables.disposed()

    init {
        disposable.add(
            Flowable.combineLatest(
                queryCuisine(),
                queryCart(),
                BiFunction(this::loadInitialData)
            )
                .subscribe(
                    { response -> },
                    { throwable -> println("Error... $throwable") })
        )
    }

    private fun loadCartInformation(restaurantWithCart: RestaurantWithCart) {
        if (currentSelectedCuisine == restaurantWithCart.cuisineId) {
            cartLiveData.postValue(restaurantWithCart.cartItems.sumBy { it.quantity })
        }
    }

    fun setCurrentCuisine(cuisineId: Int) {
        currentSelectedCuisine = cuisineId
    }

    private fun loadInitialData(response: Response, restaurantWithCart: RestaurantWithCart) {
        initialViewState.listOfCuisine.clear()
        initialViewState.listOfMustTry.clear()
        initialViewState.listOfCuisine.apply {
            response.cuisines.forEach { cuisine ->
                add(CuisineItem(cuisine.cuisineId, cuisine.cuisineName, cuisine.cuisineImage))
            }
        }
        initialViewState.listOfMustTry.apply {
            response.bestThree.forEach { cuisineBestThree ->
                cuisineBestThree.mustTry.forEach { mustTry ->
                    add(
                        MustTryItem(
                            cuisineBestThree.cuisineId,
                            mustTry.foodId,
                            mustTry.foodImageId,
                            mustTry.foodName,
                            mustTry.foodRating,
                            mustTry.foodPrice,
                            if (restaurantWithCart.cuisineId == cuisineBestThree.cuisineId)
                                restaurantWithCart.cartItems.find { it.foodId == mustTry.foodId }?.quantity
                                    ?: 0 else 0
                        )
                    )
                }
            }
        }

        cuisinesLiveData.postValue(initialViewState.listOfCuisine)
        if (initialViewState.listOfCuisine.isNotEmpty()) {
            println("currentSelectedCuisine $currentSelectedCuisine")
            currentSelectedCuisine?.let {
                getMustTry(it)
            } ?: kotlin.run {
                getMustTry(initialViewState.listOfCuisine[0].cuisineId)
            }
            loadCartInformation(restaurantWithCart)
            getCartCount()
        }
    }


    fun getMustTry(cuisineId: Int) {
        mustTryLiveData.postValue(mapMustTryForCuisine(cuisineId))
    }

    private fun mapMustTryForCuisine(cuisineId: Int) =
        initialViewState.listOfMustTry.filter { it.cuisineId == cuisineId }.toList()

    private fun getTotalItemInCart() =
        initialViewState.restaurantWithCart.cartItems.map { it.quantity }
            .sum()

    /*   fun updateCartCountForCuisine(cuisineId: Int, foodId: Int, count: Int) {
           initialViewState.restaurantWithCart.cartItems.find { it.cuisineId == cuisineId && it.foodId == foodId }?.quantity =
               count
           getCartCount(cuisineId)
       }*/

    fun getCartCount() {
        cartLiveData.postValue(getTotalItemInCart())
    }

    fun addToCart(cuisineId: Int, foodId: Int, foodName: String, foodPrice: Int, quantity: Int) {
        checkCartDisposable.dispose()
        checkCartDisposable = queryCart()
            .firstOrError()
            .subscribe(
                { response ->
                    println("response .$response")
                    if (response.cuisineId != cuisineId && response.cartItems.isNotEmpty()) {
                        println("clear old cart...")
                        cartClearLiveData.postValue(NotificationEvent(Unit))
                        clearCart()
                        commandAddToCart(
                            CommandAddToCart.Request(
                                cuisineId,
                                foodId,
                                foodName,
                                foodPrice,
                                quantity
                            )
                        )

                    } else {
                        println("addToCart...")
                        commandAddToCart(
                            CommandAddToCart.Request(
                                cuisineId,
                                foodId,
                                foodName,
                                foodPrice,
                                quantity
                            )
                        )

                    }
                },
                { throwable -> println("Error... $throwable") })

    }

    fun removeFromCart(cuisineId: Int, foodId: Int) {
        checkCartDisposable.dispose()
        checkCartDisposable = queryCart()
            .firstOrError()
            .subscribe(
                { response ->
                    commandRemoveFromCart(CommandRemoveFromCart.Request(cuisineId, foodId))
                },
                { throwable -> println("Error... $throwable") })
    }

    fun getAllCuisines() = cuisinesLiveData

    fun getMustTryOfSelected() = mustTryLiveData

    fun getCartItems() = cartLiveData

    fun getCartClearNotification() = cartClearLiveData
}
