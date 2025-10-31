package com.o7solutions.wagonwheel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class WagonWheel @JvmOverloads constructor( context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var items: List<String> = emptyList()
    private var colors: List<Int> = emptyList()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    fun setItems(newItems: List<String>) {
        items = newItems
        colors = newItems.map { getRandomColor() }
        invalidate()
    }

    private fun getRandomColor(): Int {
        val rnd = kotlin.random.Random
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    private fun onClick()  //: String
    {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (items.isEmpty()) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - 20f
        val rect = RectF(
            centerX - radius, centerY - radius,
            centerX + radius, centerY + radius
        )

        val anglePerSlice = 360f / items.size
        var startAngle = 0f

        for (i in items.indices) {
            paint.color = colors[i]
            canvas.drawArc(rect, startAngle, anglePerSlice, true, paint)

            val angle = Math.toRadians((startAngle + anglePerSlice / 2).toDouble())
            val labelX = (centerX + (radius / 1.5f) * cos(angle)).toFloat()
            val labelY = (centerY + (radius / 1.5f) * sin(angle)).toFloat()
            canvas.drawText(items[i], labelX, labelY, textPaint)

            startAngle += anglePerSlice
        }
    }
}