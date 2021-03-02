package com.example.restaurant.data

data class MustTryItem(val cuisineId: Int, val foodId: Int, val foodImage: String,
                       val foodName: String, val foodRating: Float, val foodPrice: Int, var foodCount: Int = 0){

    companion object {
        val EMPTY = MustTryItem(0, 0, "", "" ,0.0F, 0, 0)
    }
}
