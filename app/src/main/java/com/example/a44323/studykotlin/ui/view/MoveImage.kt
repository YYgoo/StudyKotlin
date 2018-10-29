package com.example.a44323.studykotlin.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.support.v7.widget.AppCompatImageView
import android.view.MotionEvent

import com.nineoldandroids.view.ViewHelper

class MoveImage : AppCompatImageView {

    internal var mLastX = 0
    internal var mLastY = 0

    internal var isMove = false


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isMove = false
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                val translationX = ViewHelper.getTranslationX(this).toInt() + deltaX
                val translationY = ViewHelper.getTranslationY(this).toInt() + deltaY
                ViewHelper.setTranslationX(this, translationX.toFloat())
                ViewHelper.setTranslationY(this, translationY.toFloat())
                isMove = true
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        mLastX = x
        mLastY = y
        return if (isMove)
            true
        else
            super.onTouchEvent(event)

    }
}
