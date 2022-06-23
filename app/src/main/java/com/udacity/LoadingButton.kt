package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates
import android.util.AttributeSet
import android.view.View
import android.graphics.Canvas
import android.graphics.RectF


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator.ofInt(0, 360)

    private var textColor by Delegates.notNull<Int>()
    private var backgroudColor by Delegates.notNull<Int>()
    private var accentColor by Delegates.notNull<Int>()
    private var loadingColor by Delegates.notNull<Int>()

    private var downloadString = context.getString(R.string.download)
    private var loadingString = context.getString(R.string.button_loading)

    private var buttonValue = ""

    private var progress = 0


    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when(new) {
            ButtonState.Loading -> {
                buttonValue = loadingString
                valueAnimator.start()
            }
            ButtonState.Completed -> {
                buttonValue = downloadString
                valueAnimator.cancel()
                progress = 0
            }
        }
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        typeface = Typeface.create( "", Typeface.NORMAL)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroudColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, 0)
            accentColor = getColor(R.styleable.LoadingButton_buttonCircleColor, 0)
        }

        buttonState = ButtonState.Completed

        valueAnimator.setDuration(2500).apply {
            addUpdateListener {
                progress = (it.animatedValue) as Int
                invalidate()
            }
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private val rect = RectF(
        740f,
        50f,
        810f,
        110f
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = backgroudColor
        canvas?.drawRect(0f,0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        paint.color = loadingColor
        canvas?.drawRect(0f, 0f, (width * progress/360f), height.toFloat(), paint)

        paint.color = accentColor
        canvas?.drawArc(rect, 0f,progress.toFloat(), true, paint)

        paint.color = textColor
        canvas?.drawText(buttonValue, (widthSize/2.0f), (heightSize/2.0f) + 20.0f, paint)

    }



}