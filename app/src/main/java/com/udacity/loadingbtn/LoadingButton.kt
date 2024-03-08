package com.udacity.loadingbtn

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import com.udacity.R
import timber.log.Timber
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText = ""
    private var progress = 0f
    private lateinit var rectF : RectF


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->


    }
    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.defaultTextSize)
        textAlign = Paint.Align.CENTER
    }



    init {
        buttonState = ButtonState.Clicked
        isClickable = true
        buttonText = resources.getString(R.string.button_download)
    }

    override fun performClick(): Boolean {
        Timber.i("btn clicked")
        if (super.performClick()) return true
        if (buttonState == ButtonState.Clicked) {
            buttonState = ButtonState.Loading
            buttonText = resources.getString(R.string.button_loading)
            animateProgressColor()
        }


        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(resources.getColor(R.color.colorPrimary))
        when (buttonState){
            ButtonState.Clicked -> {

            }
            ButtonState.Loading -> {
                canvas.save()
                paint.color = resources.getColor(R.color.colorPrimaryDark)
                paint.style = Paint.Style.FILL

                canvas.drawRect(rectF, paint)
                canvas.restore()
            }
            ButtonState.Completed -> {

            }
        }
        canvas.save()
        paint.color = Color.WHITE
        canvas.translate((widthSize / 2).toFloat(), (heightSize / 2 + paint.textSize / 2))
        canvas.drawText(buttonText, 0f, 0f, paint)
        canvas.restore()

        super.onDraw(canvas)

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
        Timber.i("width: $widthSize, height: $heightSize")
        setMeasuredDimension(w, h)
    }

    private fun animateProgressColor() {
        Timber.i("animate loading")
        val timeToDownload = 3_000L
        val timer = object: CountDownTimer(timeToDownload, 100) {
            override fun onTick(millisUntilFinished: Long) {
                progress = ((timeToDownload - millisUntilFinished).toFloat() / timeToDownload)
                Timber.i("progress: $progress - $timeToDownload, $millisUntilFinished")
                rectF = RectF(0f, 0f, progress * widthSize, heightSize.toFloat())
                invalidate()
            }
            override fun onFinish() {
                progress = 1.0f
                invalidate()
            }

        }
        timer.start()
    }


}