package com.udacity.loadingbtn

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.udacity.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 200
    private var heightSize = 100

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->



    }
    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.defaultTextSize)
        textAlign = Paint.Align.CENTER
    }
    private val rectInset = resources.getDimension(R.dimen.rectInset)




    init {
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(resources.getColor(R.color.colorPrimary))
        paint.color = Color.WHITE
        canvas.translate((widthSize/2).toFloat(), (heightSize/2 + paint.textSize/2))
        canvas.drawText(resources.getString(R.string.button_download), 0f, 0f,paint)
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
        setMeasuredDimension(w, h)
    }

    private fun animateProgressColor() {
        val animator = ObjectAnimator.ofArgb(this,"backgroundColor", Color.BLACK, Color.RED)
        animator.setDuration(500)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        //animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }

}