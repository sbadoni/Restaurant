package com.example.restaurant.util

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.restaurant.R
import kotlinx.android.synthetic.main.item_counter.view.*

private const val KEY_ORDER_COUNT = "key_order_count"
private const val KEY_SUPER_STATE = "key_super_state"

class ItemCounter: LinearLayout {
    //private var currentlySelectedItem = 0
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        LayoutInflater.from(context).inflate(R.layout.item_counter, this, true)
        /*item_decrease.setOnClickListener {
            if(currentlySelectedItem == 0)
                return@setOnClickListener
            else
                currentlySelectedItem--
            setTotalItemSelected(currentlySelectedItem)

        }
        item_increase.setOnClickListener {
            currentlySelectedItem++
            setTotalItemSelected(currentlySelectedItem)
        }
        setTotalItemSelected(currentlySelectedItem)*/
    }

    //fun getItemCount() = currentlySelectedItem

    fun getItemDecrease() = item_decrease
    fun getItemIncrease() = item_increase
    fun getItemCount() = item_currentlySelected
    fun setCount(count: Int) {
        item_currentlySelected.text = count.toString()
    }

  /*  override fun onSaveInstanceState(): Parcelable? = Bundle().apply {
        putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        putString(KEY_ORDER_COUNT, item_currentlySelected.text.toString())
    }

    override fun onRestoreInstanceState(state: Parcelable?) =
        if (state is Bundle) {
            println("onRestoreInstanceState ${state.getString(KEY_ORDER_COUNT, "")}")
            val restoredValue = state.getString(KEY_ORDER_COUNT, "")
            item_currentlySelected.setText(state.getString(KEY_ORDER_COUNT, ""), TextView.BufferType.EDITABLE)
            if(restoredValue.isNotEmpty()){
                currentlySelectedItem = restoredValue.toInt()
            }

            super.onRestoreInstanceState(state.getParcelable(KEY_SUPER_STATE))
        } else {
            super.onRestoreInstanceState(state)
        }
     fun setTotalItemSelected(totalCount: Int){
        item_currentlySelected.text = totalCount.toString()
    }*/
}
