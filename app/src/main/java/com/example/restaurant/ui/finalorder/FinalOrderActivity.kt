package com.example.restaurant.ui.finalorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.restaurant.R
import kotlinx.android.synthetic.main.activity_final_order.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FinalOrderActivity : AppCompatActivity() {
    private val finalOrderViewModel: FinalOrderViewModel by viewModel()
    private val finalOrderAdapter: FinalOrderAdapter by inject(parameters = { parametersOf(layoutInflater, applicationContext) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_order)
        initRecyclerView()
        initData()
    }

    private fun initRecyclerView(){
        recyclerView_finalOrder.run {
            val linearLayoutManager = get<LinearLayoutManager>(parameters = { parametersOf(context!!) })
            val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
            context?.let { ContextCompat.getDrawable(it, R.drawable.horizontal_divider) }?.let(dividerItemDecoration::setDrawable)
            addItemDecoration(dividerItemDecoration)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = linearLayoutManager
            adapter = finalOrderAdapter
        }
    }

    private fun initData() {
        finalOrderViewModel.observerCartData() .observe(this, Observer { items ->
            items.let {
                finalOrderAdapter.submitList(it)
                calculateFinalBill(it)
            }
        })
    }
    //TODO THIS MIGHT BE PART OF VIEW-MODEL
    private fun calculateFinalBill(listOf: List<FoodItemWithPrice>){
        val total = listOf.sumBy { it.foodPrice }
        finalPrice.text = total.toString()
        val gst = total *2.5/100
        finalPricecgst.text = gst.toString()
        finalPricesgst.text = gst.toString()
        grandTotal.text = (total + gst*2).toString()
    }
}
