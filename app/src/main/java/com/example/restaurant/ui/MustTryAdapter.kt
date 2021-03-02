package com.example.restaurant.ui

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurant.R
import com.example.restaurant.data.MustTryItem
import com.example.restaurant.util.UpdateFoodCounter
import kotlinx.android.synthetic.main.item_must_try.view.*

class MustTryAdapter(
    private val layoutInflater: LayoutInflater,
    private val resources: Resources,
    private val context: Context,
    private val updateFoodCounter: UpdateFoodCounter
) : ListAdapter<MustTryItem, RecyclerView.ViewHolder>(MustTryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MustTryViewHolder(
            layoutInflater.inflate(MustTryViewHolder.LAYOUT, parent, false),
            resources,
            context,
            updateFoodCounter
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as MustTryViewHolder).render(getItem(position))

    override fun getItemId(position: Int): Long = getItem(position).foodId.hashCode() + getItem(position).cuisineId.hashCode().toLong()

}

class MustTryViewHolder(
    private val view: View,
    private val resources: Resources,
    private val context: Context,
    private val updateFoodCounter: UpdateFoodCounter
) : RecyclerView.ViewHolder(view) {

    companion object {
        const val LAYOUT = R.layout.item_must_try
    }

    private val itemFoodImage by lazy { view.item_FoodImage }
    private val itemFoodName by lazy { view.item_FoodName }
    private val itemFoodPrice by lazy { view.item_FoodPrice }
    private val itemFoodRating by lazy { view.item_FoodRating }
    private val itemFoodCounter by lazy { view.item_FoodCounter }

    fun render(mustTryItem: MustTryItem) {
        with(mustTryItem) {
            println("render")
            itemFoodName.text = foodName
            val resourceId: Int =
                resources.getIdentifier(foodImage, "drawable", context.packageName)
            itemFoodImage.setImageResource(resourceId)
            itemFoodPrice.text = String.format(resources.getString(R.string.rupee), foodPrice)
            itemFoodRating.rating = foodRating
            itemFoodCounter.getItemCount().text = foodCount.toString()
            itemFoodCounter.getItemDecrease().setOnClickListener {
                val count = itemFoodCounter.getItemCount().text.toString().toInt()
                if (count == 0)
                    return@setOnClickListener
                itemFoodCounter.setCount(count)
                foodCount--
                itemFoodCounter.getItemCount().text = foodCount.toString()
                updateFoodCounter.removeFromCart(cuisineId, foodId, foodCount)

            }
            itemFoodCounter.getItemIncrease().setOnClickListener {
                foodCount++
                itemFoodCounter.getItemCount().text = foodCount.toString()
                //itemFoodCounter.setCount(count)
                updateFoodCounter.addToCart(cuisineId, foodId, foodName, foodPrice, foodCount)
            }
        }
    }
}


class MustTryDiffCallback : DiffUtil.ItemCallback<MustTryItem>() {
    override fun areItemsTheSame(oldItem: MustTryItem, newItem: MustTryItem): Boolean =
        oldItem.foodId == newItem.foodId

    override fun areContentsTheSame(oldItem: MustTryItem, newItem: MustTryItem): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: MustTryItem, newItem: MustTryItem): Any? =
        Pair(oldItem, newItem)
}
