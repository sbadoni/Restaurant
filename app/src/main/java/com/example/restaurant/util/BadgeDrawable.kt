package com.example.restaurant.util

import android.R
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class BadgeDrawable(context: Context) : Drawable() {

    private lateinit var mBadgePaint: Paint
    private lateinit var mBadgePaint1: Paint
    private lateinit var mTextPaint: Paint
    private val mTxtRect: Rect = Rect()

    private var mCount = ""
    private var mWillDraw = false

    init {
        val mTextSize: Float = context.resources.getDimension(com.example.restaurant.R.dimen.cart_total_item)
        mBadgePaint = Paint()
        mBadgePaint.color = Color.RED
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL
        mBadgePaint1 = Paint()
        mBadgePaint1.color = ContextCompat.getColor(
            context.applicationContext,
            R.color.holo_blue_dark
        )
        mBadgePaint1.isAntiAlias = true
        mBadgePaint1.style = Paint.Style.FILL
        mTextPaint = Paint()
        mTextPaint.color = Color.WHITE
        mTextPaint.typeface = Typeface.DEFAULT
        mTextPaint.textSize = mTextSize
        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        if (!mWillDraw) {
            return
        }
        val bounds = bounds
        val width = (bounds.right - bounds.left).toFloat()
        val height = (bounds.bottom - bounds.top).toFloat()

        // Position the badge in the top-right quadrant of the icon.

        /*Using Math.max rather than Math.min */


        // Position the badge in the top-right quadrant of the icon.

        /*Using Math.max rather than Math.min */
        val radius = Math.max(width, height) / 2 / 2
        val centerX = width - radius - 1 + 5
        val centerY = radius - 5
        if (mCount.length <= 2) {
            // Draw badge circle.
            canvas.drawCircle(centerX, centerY, (radius + 7.5).toFloat(), mBadgePaint1)
            canvas.drawCircle(centerX, centerY, (radius + 5.5).toFloat(), mBadgePaint)
        } else {
            canvas.drawCircle(centerX, centerY, (radius + 8.5).toFloat(), mBadgePaint1)
            canvas.drawCircle(centerX, centerY, (radius + 6.5).toFloat(), mBadgePaint)
//	        	canvas.drawRoundRect(radius, radius, radius, radius, 10, 10, mBadgePaint);
        }
        // Draw badge count text inside the circle.
        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length, mTxtRect)
        val textHeight = mTxtRect.bottom - mTxtRect.top.toFloat()
        val textY = centerY + textHeight / 2f
        if (mCount.length > 2) canvas.drawText(
            "99+",
            centerX,
            textY,
            mTextPaint
        ) else canvas.drawText(mCount, centerX, textY, mTextPaint)
    }

    fun setCount(count: String) {
        mCount = count

        // Only draw a badge if there are notifications.
        mWillDraw = !count.equals("0", ignoreCase = true)
        invalidateSelf()
    }

    override fun setAlpha(p0: Int) {
    }

    override fun getOpacity(): Int = PixelFormat.UNKNOWN

    override fun setColorFilter(p0: ColorFilter?) {
    }
}
