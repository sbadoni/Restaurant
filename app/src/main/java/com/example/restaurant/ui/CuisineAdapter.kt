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
import com.example.restaurant.data.CuisineItem
import kotlinx.android.synthetic.main.item_cuisine.view.*

class CuisineAdapter(private val layoutInflater: LayoutInflater,
                     private val resources: Resources,
                     private val context: Context,
                     private val onCuisineClicked: OnCuisineClicked) : ListAdapter<CuisineItem, RecyclerView.ViewHolder>(CuisineDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CuisineViewHolder(layoutInflater.inflate(CuisineViewHolder.LAYOUT, parent, false), resources, context, onCuisineClicked)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as CuisineViewHolder).render(getItem(position))

    override fun getItemId(position: Int): Long = getItem(position).cuisineId.hashCode().toLong()

}

class CuisineViewHolder(private val view : View,
                        private val resources: Resources,
                        private val context: Context,
                        private val onCuisineClicked: OnCuisineClicked): RecyclerView.ViewHolder(view) {

    companion object {
        const val LAYOUT = R.layout.item_cuisine
    }
    private val itemCuisineName by lazy { view.item_CuisineName }
    private val itemCuisineImage by lazy { view.item_CuisineImage }
    var cuisineId: Int = 0
    fun render(cuisineItem: CuisineItem) {
        with(cuisineItem){
            this@CuisineViewHolder.cuisineId = cuisineId
            itemCuisineName.text = cuisineName
            val resourceId: Int = resources.getIdentifier(cuisineImage,"drawable", context.packageName)
            itemCuisineImage.setImageResource(resourceId)
            view.setOnClickListener {
                println("sameer $cuisineId")
                onCuisineClicked.onClick(cuisineId)
            }
        }
    }
}

interface OnCuisineClicked{
    fun onClick(cuisineId: Int)
}


class CuisineDiffCallback : DiffUtil.ItemCallback<CuisineItem>() {
    override fun areItemsTheSame(oldItem: CuisineItem, newItem: CuisineItem): Boolean = oldItem.cuisineId == newItem.cuisineId

    override fun areContentsTheSame(oldItem: CuisineItem, newItem: CuisineItem): Boolean = oldItem == newItem
}
