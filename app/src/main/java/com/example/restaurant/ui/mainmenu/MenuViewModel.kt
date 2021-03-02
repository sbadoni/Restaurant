package com.example.restaurant.ui.mainmenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurant.data.CuisineItem
import com.example.restaurant.data.MustTryItem
import com.example.restaurant.data.Response
import com.example.restaurant.data.RestaurantWithCart
import com.example.restaurant.ui.MainViewState
import com.example.restaurant.usecase.*
import com.example.restaurant.util.NotificationEvent
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction

class MenuViewModel(private val queryCuisine: QueryCuisine,
                    private val cuisineId: Int,
                    private val queryCart: QueryCart,
                    private val commandAddToCart: CommandAddToCart,
                    private val clearCart: CommandClearCart,
                    private val commandRemoveFromCart: CommandRemoveFromCart
): ViewModel() {

    private val cartLiveData = MutableLiveData<Int>()
    private val menuLiveData = MutableLiveData<List<MenuItem>>()
    private val cuisineImageLiveData = MutableLiveData<CuisineData>()
    private val cartClearLiveData = MutableLiveData<NotificationEvent<Unit>>()
    private val disposable = CompositeDisposable()
    private var initialViewState: MenuViewState = MenuViewState.EMPTY
    private var checkCartDisposable = Disposables.disposed()
    init {
        disposable.add(
            Flowable.combineLatest(queryCuisine(),
                queryCart(),
                BiFunction(this::loadInitialData))
                .subscribe(
                    { response ->  },
                    { throwable -> println("Error... $throwable") })
        )


      /*  disposable.add(
            queryCuisine()
                .firstOrError()
                .subscribe(
                    { response -> loadInitialData(response) },
                    { throwable -> println("Error... $throwable") })
        )*/
    }

    private fun loadInitialData(response: Response, restaurantWithCart: RestaurantWithCart) {
        initialViewState.menuItems.clear()
        initialViewState.menuItems.apply {
            response.menu.forEach { menu ->
                if (cuisineId == menu.cuisineId) {
                    menu.foodMenu.forEach { foodMenu ->
                        println(foodMenu)
                        add(MenuItem(menu.cuisineId,
                            foodMenu.foodId,
                            foodMenu.foodName,
                            foodMenu.foodImageId,
                            foodMenu.foodPrice,
                            if (restaurantWithCart.cuisineId == menu.cuisineId)
                                restaurantWithCart.cartItems.find { it.foodId == foodMenu.foodId }?.quantity
                                    ?: 0 else 0
                        )
                        )
                    }
                }
            }
        }
        cuisineImageLiveData.postValue(findCuisineImage(cuisineId, response))
        menuLiveData.postValue(initialViewState.menuItems)
        getCartCount()
    }

   /* fun updateCartCountForCuisine(cuisineId: Int, foodId: Int, count: Int){
        initialViewState.menuItems.find {  it.foodId == foodId}?.cartCount = count
        getCartCount()
    }*/
    private fun getCartCount(){
        cartLiveData.postValue(getTotalItemInCart())
    }

    private fun getTotalItemInCart() =
        initialViewState.restaurantWithCart.cartItems.map { it.quantity }
            .sum()

    private fun findCuisineImage(cuisineId: Int, response: Response): CuisineData {
        val find = response.cuisines.find { it.cuisineId == cuisineId }
        return CuisineData(find?.cuisineName?: "", find?.cuisineImage ?: "")
    }

    fun addToCart(cuisineId: Int, foodId: Int, foodName: String, foodPrice: Int, quantity: Int){
        checkCartDisposable.dispose()
        checkCartDisposable = queryCart()
            .firstOrError()
            .subscribe(
                { response ->
                    println("response .$response")
                    if(response.cuisineId != cuisineId && response.cartItems.isNotEmpty()){
                        println("clear old cart...")
                        cartClearLiveData.postValue(NotificationEvent(Unit))
                        clearCart()
                        commandAddToCart(
                            CommandAddToCart.Request(cuisineId, foodId, foodName, foodPrice, quantity))

                    }
                    else{
                        println("addToCart...")
                        commandAddToCart(CommandAddToCart.Request(cuisineId, foodId, foodName, foodPrice, quantity))

                    }
                },
                { throwable -> println("Error... $throwable") })

    }

    fun removeFromCart(cuisineId: Int, foodId: Int){
        checkCartDisposable.dispose()
        checkCartDisposable = queryCart()
            .firstOrError()
            .subscribe(
                { response ->
                    commandRemoveFromCart(CommandRemoveFromCart.Request(cuisineId, foodId))
                },
                { throwable -> println("Error... $throwable") })
    }



    fun getCartItems() = cartLiveData

    fun getMenu() = menuLiveData

    fun getCuisineImage() = cuisineImageLiveData

    fun getCartClearNotification() = cartClearLiveData
}
