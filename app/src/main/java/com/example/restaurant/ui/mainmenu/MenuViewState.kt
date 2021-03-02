package com.example.restaurant.ui.mainmenu

import com.example.restaurant.data.RestaurantWithCart


class MenuViewState(val menuItems: MutableList<MenuItem>,
                    val restaurantWithCart: RestaurantWithCart
){
    companion object {
        val EMPTY = MenuViewState(mutableListOf(), RestaurantWithCart.EMPTY)
    }
}

data class MenuItem(
    val cuisineId: Int,
    val foodId: Int,
    val foodName: String,
    val foodImageId: String,
    val foodPrice: Int,
    var cartCount: Int
){
    companion object {
        val EMPTY = MenuItem(1,1, "", "" ,1, 0)
    }
}

data class CuisineData(val cuisineName: String, val cuisineImage: String)

