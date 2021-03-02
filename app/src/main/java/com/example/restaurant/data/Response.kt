package com.example.restaurant.data

import com.google.gson.annotations.SerializedName

data class Response(@SerializedName("status")
                    val status: Int,
                    @SerializedName("cuisines")
                    val cuisines: List<Cuisine>,
                    @SerializedName("bestthree")
                    val bestThree: List<CuisineBestThree>,
                    @SerializedName("menu")
                    val menu: List<Menu>){

    companion object {
        val EMPTY = Response(-1 , listOf(), listOf(), listOf())
    }
}

data class Cuisine(val cuisineId: Int, val cuisineName: String, val cuisineImage: String)
data class CuisineBestThree(
    @SerializedName("cuisineId")
    val cuisineId: Int,
    @SerializedName("musttry")
    val mustTry: List<MustTry>)
data class MustTry(
    @SerializedName("foodId")
    val foodId: Int,
    @SerializedName("foodName")
    val foodName: String,
    @SerializedName("foodImageId")
    val foodImageId: String,
    @SerializedName("foodPrice")
    val foodPrice: Int,
    @SerializedName("foodrating")
    val foodRating: Float)

data class Menu(
    @SerializedName("cuisineId")
    val cuisineId: Int,
    @SerializedName("foodMenu")
    val foodMenu: List<FoodMenu>
)

data class FoodMenu(
    @SerializedName("foodId")
    val foodId: Int,
    @SerializedName("foodName")
    val foodName: String,
    @SerializedName("foodImageId")
    val foodImageId: String,
    @SerializedName("foodPrice")
    val foodPrice: Int
)
