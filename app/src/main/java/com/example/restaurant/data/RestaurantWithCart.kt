package com.example.restaurant.data

data class RestaurantWithCart(var cuisineId: Int, var cartItems: MutableList<ItemInCart>){
    companion object {
        val EMPTY = RestaurantWithCart(-1, mutableListOf())
    }
}
