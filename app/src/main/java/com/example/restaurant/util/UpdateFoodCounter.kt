package com.example.restaurant.util

interface UpdateFoodCounter {
    fun addToCart(cuisineId: Int, foodId: Int, foodName: String, foodPrice: Int, quantity: Int)
    fun removeFromCart(cuisineId: Int, foodId: Int, count: Int)
}
