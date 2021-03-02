package com.example.restaurant.ui.mainmenu

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurant.R
import com.example.restaurant.util.UpdateFoodCounter
import kotlinx.android.synthetic.main.item_menu.view.*

class MenuAdapter(private val layoutInflater: LayoutInflater,
                  private val resources: Resources,
                  private val context: Context,
                  private val updateFoodCounter: UpdateFoodCounter
) : ListAdapter<MenuItem, RecyclerView.ViewHolder>(MenuDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MenuViewHolder(layoutInflater.inflate(MenuViewHolder.LAYOUT, parent, false), resources, context, updateFoodCounter)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as MenuViewHolder).render(getItem(position))

    override fun getItemId(position: Int): Long = getItem(position).foodId.hashCode().toLong()

}
class MenuViewHolder(private val view : View,
                    private val resources: Resources,
                    private val context: Context, private val updateFoodCounter: UpdateFoodCounter
): RecyclerView.ViewHolder(view) {

    companion object {
        const val LAYOUT = R.layout.item_menu
    }
    private val itemFoodImage by lazy { view.menuItem_FoodImage }
    private val itemFoodName by lazy { view.menuItem_FoodName }
    private val itemFoodPrice by lazy { view.menuItem_FoodPrice}
    private val itemFoodCounter by lazy { view.menuItem_FoodCounter }

    fun render(menuItem: MenuItem) {
        with(menuItem){
            itemFoodName.text = foodName
            val resourceId: Int = resources.getIdentifier(foodImageId,"drawable", context.packageName)
            itemFoodImage.setImageResource(resourceId)
            itemFoodPrice.text = foodPrice.toString()
            itemFoodCounter.getItemCount().text = cartCount.toString()
            itemFoodCounter.getItemDecrease().setOnClickListener {
                val count = itemFoodCounter.getItemCount().text.toString().toInt()
                if(count == 0)
                    return@setOnClickListener
                itemFoodCounter.setCount(count)
                cartCount--
                itemFoodCounter.getItemCount().text = cartCount.toString()
                updateFoodCounter.removeFromCart(cuisineId, foodId, cartCount)

            }
            itemFoodCounter.getItemIncrease().setOnClickListener {
                cartCount++
                itemFoodCounter.getItemCount().text = cartCount.toString()
                //itemFoodCounter.setCount(count)
                updateFoodCounter.addToCart(cuisineId, foodId, foodName, foodPrice, cartCount)
            }
        }
    }
}

class MenuDiffCallback : DiffUtil.ItemCallback<MenuItem>() {
    override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean = oldItem.foodId == newItem.foodId

    override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean = oldItem == newItem
}
