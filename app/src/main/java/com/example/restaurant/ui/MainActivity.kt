package com.example.restaurant.ui

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
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.restaurant.R
import com.example.restaurant.ui.finalorder.FinalOrderActivity
import com.example.restaurant.ui.mainmenu.MenuActivity
import com.example.restaurant.util.*
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()
    private val cuisineAdapter: CuisineAdapter by inject(parameters = { parametersOf(layoutInflater, applicationContext, onCuisineClicked) })
    private val mustTryAdapter: MustTryAdapter by inject(parameters = { parametersOf(layoutInflater, applicationContext, updateFoodCounter) })
    private lateinit var textCartItemCount: TextView

    private val snapHelper = PagerSnapHelper()
    private val snapChangeListener = object : OnSnapPositionChangeListener{
        override fun onSnapPositionChange(position: Int) {
           val cuisineViewHolder: CuisineViewHolder = recyclerView.findViewHolderForAdapterPosition(position) as CuisineViewHolder
            println("onSnapPositionChange position = $position  cuisineId = ${cuisineViewHolder.cuisineId}")
            mainViewModel.getMustTry(cuisineViewHolder.cuisineId)
            mainViewModel.getCartCount()
            mainViewModel.setCurrentCuisine(cuisineViewHolder.cuisineId)
        }
    }

    private val updateFoodCounter = object : UpdateFoodCounter {
        override fun addToCart(cuisineId: Int, foodId: Int, foodName: String, foodPrice: Int, quantity: Int) {
            println("addToCart cuisineId = $cuisineId  foodId = $foodId quantity = $quantity")
            mainViewModel.addToCart(cuisineId, foodId, foodName, foodPrice, quantity )
        }

        override fun removeFromCart(cuisineId: Int, foodId: Int, count: Int) {
            println("removeFromCart $cuisineId  $foodId $count")
            mainViewModel.removeFromCart(cuisineId, foodId)
        }
    }

    private val onCuisineClicked = object: OnCuisineClicked{
        override fun onClick(cuisineId: Int) {
           startActivity(Intent(this@MainActivity, MenuActivity::class.java).apply {
               putExtra("cuisineId", cuisineId)
           })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            if(textCartItemCount.visibility == View.VISIBLE)
            startActivity(Intent(this@MainActivity, FinalOrderActivity::class.java))
        }
        initCart()
        return true
    }

    private fun initRecyclerView(){
        recyclerView.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = cuisineAdapter
        }
        recyclerView.attachSnapHelperWithListener(snapHelper, SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE, snapChangeListener)

        recyclerView_MustTry.run {
            val linearLayoutManager = get<LinearLayoutManager>(parameters = { parametersOf(context!!) })
            val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
            context?.let { ContextCompat.getDrawable(it, R.drawable.horizontal_divider) }?.let(dividerItemDecoration::setDrawable)
            addItemDecoration(dividerItemDecoration)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = linearLayoutManager
            adapter = mustTryAdapter
        }
    }

    private fun initData() {
        snapHelper.getSnapPosition(recyclerView)
        mainViewModel.getAllCuisines().observe(this, Observer { response ->
            response?.let {
                cuisineAdapter.submitList(it)
            }
        })
        mainViewModel.getMustTryOfSelected().observe(this, Observer { mustTryItems ->
            mustTryItems.let {
                mustTryAdapter.submitList(it)
            }
        })
    }

    private fun initCart(){
        mainViewModel.getCartItems().observe(this, Observer{ itemInCart ->
            if(::textCartItemCount.isInitialized){
                //textCartItemCount.text = itemInCart.toString()
                setupBadge(itemInCart)
            }
        })

        mainViewModel.getCartClearNotification().observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                Toast.makeText(this,"Clearing cart better use Alert when we have time", Toast.LENGTH_LONG).show()
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
                textCartItemCount.text = count.toString()

                if (textCartItemCount.visibility != View.VISIBLE) {
                    textCartItemCount.visibility = View.VISIBLE
                }
            }
        }
    }
}
