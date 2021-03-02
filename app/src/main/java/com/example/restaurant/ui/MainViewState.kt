package com.example.restaurant.ui

import com.example.restaurant.data.CuisineItem
import com.example.restaurant.data.ItemInCart
import com.example.restaurant.data.MustTryItem
import com.example.restaurant.data.RestaurantWithCart

class MainViewState(val listOfCuisine: MutableList<CuisineItem>,
                    val listOfMustTry: MutableList<MustTryItem>,
                    val restaurantWithCart: RestaurantWithCart
){
    companion object {
       val EMPTY = MainViewState(mutableListOf(), mutableListOf(), RestaurantWithCart.EMPTY)
    }
}
