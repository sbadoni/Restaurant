package com.example.restaurant.ui.finalorder

class FinalOrderViewState(val listOfFoodWithPrice: MutableList<FoodItemWithPrice>){
    companion object {
       val EMPTY = FinalOrderViewState(mutableListOf())
    }
}

data class FoodItemWithPrice(val foodId: Int, val foodName: String, val foodPrice: Int)
