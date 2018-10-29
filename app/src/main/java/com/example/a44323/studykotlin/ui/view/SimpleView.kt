package com.example.a44323.studykotlin.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.a44323.studykotlin.R

class SimpleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val mPaint: Paint

    private var progress: Int = 0

    private var bgcolor: Int = 0

    private var procolor: Int = 0

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    init {
        initAttrs(attrs)

        mPaint = Paint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mWidth, mHeight)
    }

    private fun initAttrs(attrs: AttributeSet?) {

        if (attrs == null) {

            return

        }

        mWidth = 400

        mHeight = 30

        //获得所有的属性

        val array = context.obtainStyledAttributes(attrs, R.styleable.SimpleView)

        progress = array.getInteger(R.styleable.SimpleView_progress, 0)

        bgcolor = array.getColor(R.styleable.SimpleView_bgcolor, Color.GRAY)

        procolor = array.getColor(R.styleable.SimpleView_procolor, Color.CYAN)

        array.recycle()//回收TypedArray

    }


    override fun onDraw(canvas: Canvas) {

        mPaint.color = bgcolor//设置进度条背景颜色

        val rectF = RectF()//背景的矩形

        rectF.set(3f, 3f, (mWidth - 3).toFloat(), (mHeight - 3).toFloat())

        canvas.drawRoundRect(rectF, 3f, 3f, mPaint)

        mPaint.color = procolor

        val proRect = RectF()//进度的矩形

        proRect.set(3f, 3f, mWidth * (progress.toFloat() / 100), (mHeight - 3).toFloat())

        canvas.drawRoundRect(proRect, 3f, 3f, mPaint)

    }

    fun setProgress(progress: Int) {

        if (progress < 0 || progress > 100) {
            return
        }

        this.progress = progress

        //重绘组件
        postInvalidate()

    }


}
