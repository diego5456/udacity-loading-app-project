package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0f
    private var heightSize = 0f
    private var textButton = resources.getString(R.string.button_name)
    private var backgroundColor = 0
    private var valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private var progress = 0f

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color = Color.BLACK
    }
    private val textHeight: Float = textPaint.descent() - textPaint.ascent()
    private val textOffset: Float = textHeight / 2 - textPaint.descent()

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundColor = getColor(R.styleable.LoadingButton_button_color, 0)
        }
        valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 2000
        valueAnimator.addUpdateListener {
            progress = valueAnimator.animatedValue as Float
            invalidate()
        }
    }


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                textButton = resources.getString(R.string.button_loading)
                isClickable =false
                valueAnimator.start()

            }

            ButtonState.Completed -> {
                valueAnimator.doOnEnd {
                    textButton = resources.getString(R.string.button_name)
                    progress = 0f
                    isClickable = true
                    invalidate()
                }

            }

            else -> {}
        }

    }

    fun changeState(state: ButtonState) {
        buttonState = state
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = context.getColor(R.color.button_color)
        canvas.drawRect(0f, 0f, widthSize, heightSize, paint)
        val textX = widthSize * 0.5f
        val textY = heightSize * 0.5f + textOffset
        if (progress > 0) {
            paint.color = context.getColor(R.color.button_loading_color)
            val loadingWidth = widthSize * progress
            canvas.drawRect(0f, 0f, loadingWidth, heightSize, paint)
            textPaint.color = context.getColor(R.color.white)
        } else{
            textPaint.color = context.getColor(R.color.black)
        }
        var textLength = textPaint.measureText(textButton, 0, textButton.length - 1)
        val left = (textX + textLength) - 50f
        val top = textY + textPaint.ascent()
        val right = left + 70f
        val bottom = textY + textPaint.descent()
        paint.color = Color.YELLOW
//        canvas.drawCircle(circleRight, circleBottom, 50f, paint)
        val rect = RectF(left, top, right, bottom)
        canvas.drawArc(rect, 0f, 360f * progress , true, paint)
        canvas.drawText(textButton, textX, textY, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w.toFloat()
        heightSize = h.toFloat()
        setMeasuredDimension(w, h)
    }

}