package com.example.restaurant.ui.finalorder

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurant.R
import kotlinx.android.synthetic.main.item_final_order.view.*

class FinalOrderAdapter(
    private val layoutInflater: LayoutInflater,
    private val resources: Resources
) : ListAdapter<FoodItemWithPrice, RecyclerView.ViewHolder>(FinalOrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FinalOrderViewHolder(
            layoutInflater.inflate(FinalOrderViewHolder.LAYOUT, parent, false), resources
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as FinalOrderViewHolder).render(getItem(position))

    override fun getItemId(position: Int): Long = getItem(position).foodId.toLong()

}

class FinalOrderViewHolder(
    private val view: View,
    private val resources: Resources
) : RecyclerView.ViewHolder(view) {

    companion object {
        const val LAYOUT = R.layout.item_final_order
    }

    private val itemFoodNameName by lazy { view.finalFoodName }
    private val itemFoodMultiply by lazy { view.finalFoodMultiply }
    private val itemFoodNamePrice by lazy { view.finalFoodPrice }
    fun render(foodItemWithPrice: FoodItemWithPrice) {
        with(foodItemWithPrice) {
            itemFoodMultiply.text = String.format(resources.getString(R.string.multiply), quantity)
            itemFoodNameName.text = foodName
            itemFoodNamePrice.text = (foodPrice * quantity).toString()
        }
    }
}


class FinalOrderDiffCallback : DiffUtil.ItemCallback<FoodItemWithPrice>() {
    override fun areItemsTheSame(oldItem: FoodItemWithPrice, newItem: FoodItemWithPrice): Boolean =
        oldItem.foodId == newItem.foodId

    override fun areContentsTheSame(
        oldItem: FoodItemWithPrice,
        newItem: FoodItemWithPrice
    ): Boolean = oldItem == newItem
}
