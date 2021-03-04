package com.example.restaurant.ui.finalorder

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.restaurant.R
import com.example.restaurant.util.ContextUtils
import kotlinx.android.synthetic.main.activity_final_order.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class FinalOrderActivity : AppCompatActivity() {
    private val finalOrderViewModel: FinalOrderViewModel by viewModel()
    private val finalOrderAdapter: FinalOrderAdapter by inject(parameters = { parametersOf(layoutInflater, applicationContext) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_order)
        placeYourOrder.setOnClickListener {
            Toast.makeText(this, R.string.place_order_toast, Toast.LENGTH_LONG).show()
        }
        initRecyclerView()
        initData()
    }

    private fun initRecyclerView(){
        recyclerView_finalOrder.run {
            val linearLayoutManager =
                get<LinearLayoutManager>(parameters = { parametersOf(context!!) })
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            context?.let { ContextCompat.getDrawable(it, R.drawable.horizontal_divider) }
                ?.let(dividerItemDecoration::setDrawable)
            addItemDecoration(dividerItemDecoration)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = linearLayoutManager
            adapter = finalOrderAdapter
        }
    }

    override fun attachBaseContext(base: Context) {
        println("attachBaseContext ${ContextUtils.getSavedLanguage(base)}")
        val savedLang = ContextUtils.getSavedLanguage(base)
        val config = Configuration()
        config.setLocale(Locale(savedLang))
        applyOverrideConfiguration(config)
        super.attachBaseContext(ContextUtils.onAttach(base))
    }

    private fun initData() {
        finalOrderViewModel.observerCartData().observe(this, Observer { items ->
            items.let {
                finalOrderAdapter.submitList(it)
                calculateFinalBill(it)
            }
        })
    }

    //TODO THIS MIGHT BE PART OF VIEW-MODEL
    private fun calculateFinalBill(listOf: List<FoodItemWithPrice>) {
        val total = listOf.sumBy { it.foodPrice * it.quantity }.toFloat()
        finalPrice.text = total.toString()
        val gst = total * 2.5 / 100
        finalPricecgst.text = gst.toString()
        finalPricesgst.text = gst.toString()
        grandTotal.text = (total + gst * 2).toString()
    }
}
