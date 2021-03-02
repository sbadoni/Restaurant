package com.example.restaurant.data

data class ItemInCart(
    val cuisineId: Int,
    val foodId: Int,
    val foodName: String,
    val foodPrice: Int,
    var quantity: Int
) {
    companion object {
        val EMPTY = ItemInCart(-1, -1, "", -1, -1)
    }
}
