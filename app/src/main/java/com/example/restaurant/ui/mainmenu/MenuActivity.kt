package com.example.restaurant.ui.mainmenu

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.restaurant.R
import com.example.restaurant.ui.finalorder.FinalOrderActivity
import com.example.restaurant.util.UpdateFoodCounter
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.item_cuisine.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.lang.String

class MenuActivity : AppCompatActivity() {

    private val menuViewModel: MenuViewModel by viewModel(parameters = { parametersOf(cuisineId)})
    private val menuAdapter: MenuAdapter by inject(parameters = { parametersOf(layoutInflater, applicationContext, updateFoodCounter) })
    private lateinit var textCartItemCount: TextView
    private var cuisineId: Int = 0
    private val updateFoodCounter = object : UpdateFoodCounter {
        override fun addToCart(cuisineId: Int, foodId: Int, foodName: kotlin.String, foodPrice: Int, quantity: Int) {
            println("addToCart cuisineId = $cuisineId  foodId = $foodId quantity = $quantity")
            menuViewModel.addToCart(cuisineId, foodId, foodName, foodPrice, quantity )
        }

        override fun removeFromCart(cuisineId: Int, foodId: Int, count: Int) {
            println("removeFromCart $cuisineId  $foodId $count")
            menuViewModel.removeFromCart(cuisineId, foodId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        cuisineId = intent.getIntExtra("cuisineId", 0)
        initRecyclerView()
        initData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val itemCart = menu.findItem(R.id.action_cart)
        val actionView: View = itemCart.actionView
        textCartItemCount = actionView.findViewById(R.id.cart_badge)
        actionView.findViewById<ImageView>(R.id.cart_icon)
            .setOnClickListener {
                if (textCartItemCount.visibility == View.VISIBLE)
                    startActivity(Intent(this@MenuActivity, FinalOrderActivity::class.java))
            }
        initCart()
        return true
    }

    private fun initCart(){
        menuViewModel.getCartItems().observe(this, Observer{ itemInCart ->
            if(::textCartItemCount.isInitialized){
                //textCartItemCount.text = itemInCart.toString()
                setupBadge(itemInCart)
            }
        })
    }

    private fun setupBadge(count: Int) {
        if (::textCartItemCount.isInitialized) {
            if (count == 0) {
                if (textCartItemCount.visibility != View.GONE) {
                    textCartItemCount.visibility = View.GONE
                }
            } else {
                textCartItemCount.text = String.valueOf(
                    count.coerceAtMost(99)
                )
                if (textCartItemCount.visibility != View.VISIBLE) {
                    textCartItemCount.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initRecyclerView(){
        recyclerView_Menu.run {
            val linearLayoutManager = get<LinearLayoutManager>(parameters = { parametersOf(context!!) })
            val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
            context?.let { ContextCompat.getDrawable(it, R.drawable.horizontal_divider) }?.let(dividerItemDecoration::setDrawable)
            addItemDecoration(dividerItemDecoration)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = linearLayoutManager
            adapter = menuAdapter
        }
    }

    private fun initData() {
        menuViewModel.getMenu().observe(this, Observer { response ->
            response?.let {
                menuAdapter.submitList(it)
            }
        })

        menuViewModel.getCuisineImage().observe(this, Observer { image ->
            image?.let { cuisineData->
                val resourceId: Int = resources.getIdentifier(cuisineData.cuisineImage,"drawable", applicationContext.packageName)
                menuCuisine.item_CuisineImage.setImageResource(resourceId)
                menuCuisine.item_CuisineName.text = cuisineData.cuisineName
            }
        })

        menuViewModel.getCartClearNotification().observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                Toast.makeText(this,"Clearing cart better use Alert when we have time", Toast.LENGTH_LONG).show()
            }
        })
    }
}
