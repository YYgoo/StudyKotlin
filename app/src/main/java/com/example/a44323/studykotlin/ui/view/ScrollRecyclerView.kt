package com.example.a44323.studykotlin.ui.view

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.Scroller
import android.widget.TextView
import com.example.a44323.studykotlin.Adapter.TestRecyclerAdapter

class ScrollRecyclerView  :RecyclerView{

    //当前按下的item
    var mPosition:Int? = -1
    //上一次触摸的坐标点
    var mLastX:Int? = 0
    var mLastY:Int? = 0

    var mLinearLayout:LinearLayout? = null

    var mDelete:TextView? = null

    var mMaxLenght:Int = 0
    var isDragging:Boolean = false
    var isItemMoving:Boolean = false
    var isStarScroll:Boolean = false
    var mDeleteBtnState:Int = 0
    var mVelocityTracker:VelocityTracker? = null
    var mScroller:Scroller? = null

    var mContext:Context? = null

    var mListenerDelete :((position:Int) -> Unit)? = null
    var mListenerClick :((position:Int) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
    fun init(context:Context){
        this.mContext = context
        mScroller = Scroller(context, LinearInterpolator())
        mVelocityTracker = VelocityTracker.obtain()
    }
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
    }

    override fun onDraw(c: Canvas?) {
        super.onDraw(c)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {

        var x:Float = e!!.getX()
        var y:Float = e!!.getY()

        when(e!!.action){

            MotionEvent.ACTION_DOWN -> {

                if (mDeleteBtnState == 0){

                    var v:View? = findChildViewUnder(x,y)
                    if (v == null)
                        return false

                    var holder:TestRecyclerAdapter.VH  = getChildViewHolder(v) as TestRecyclerAdapter.VH

                    mLinearLayout = holder.itemLayout

                    mPosition = holder.adapterPosition

                    mDelete = holder.deleteText

                    mMaxLenght = mDelete!!.width

                    mDelete!!.setOnClickListener {
                        mLinearLayout!!.scrollTo(0,0)
                        mDeleteBtnState = 0
                        mListenerDelete!!.invoke(mPosition!!)
                    }

                }
                else if(mDeleteBtnState == 3){
                    mScroller!!.startScroll(mLinearLayout!!.scrollX,0,-mMaxLenght,0,200)
                    mDeleteBtnState = 0
                    invalidate()
                    return false
                }
                else
                    return false


            }
            MotionEvent.ACTION_MOVE ->{
                var dx:Int = mLastX!!.minus(x.toInt())
                var dy:Int = mLastY!!.minus(y.toInt())
                var scrollX:Int = mLinearLayout!!.scrollX
                if (Math.abs(dx) > Math.abs(dy)){
                    isItemMoving = true
                    if (scrollX+dx <= 0){//左边界检测

                        mLinearLayout!!.scrollTo(0,0)
                        return true

                    }else if (scrollX+dx >= mMaxLenght){
                        mLinearLayout!!.scrollTo(mMaxLenght,0)
                        return true
                    }
                    mLinearLayout!!.scrollBy(dx,0)

                }

            }
            MotionEvent.ACTION_UP ->{

                if (!isItemMoving && !isDragging && mListenerClick != null){
                    //点击事件
                    mListenerClick!!.invoke(mPosition!!)
                }
                isItemMoving = false

                mVelocityTracker!!.computeCurrentVelocity(1000)

                var xVelocity:Float = mVelocityTracker!!.xVelocity
                var yVelocity:Float = mVelocityTracker!!.yVelocity

                var deltaX:Int = 0
                var upScroll:Int = mLinearLayout!!.scrollX

                if (Math.abs(xVelocity) > 100&&Math.abs(xVelocity)>Math.abs(yVelocity)){
                    if (xVelocity <= -100){
                        deltaX = mMaxLenght - upScroll
                        mDeleteBtnState = 2
                    }
                    else if (xVelocity > 100){
                        deltaX = -upScroll
                        mDeleteBtnState = 1
                    }
                }
                else{
                    if (upScroll >= mMaxLenght/2){

                        deltaX = mMaxLenght - upScroll
                        mDeleteBtnState = 2

                    }else if (upScroll < mMaxLenght/2){
                        deltaX = -upScroll
                        mDeleteBtnState = 1

                    }

                }
                mScroller!!.startScroll(upScroll,0,deltaX,0,200)
                isStarScroll = true
                invalidate()
                mVelocityTracker!!.clear()

            }


        }

        mLastX = x.toInt()
        mLastY = y.toInt()

        return super.onTouchEvent(e)

    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()){

            mLinearLayout!!.scrollTo(mScroller!!.currX,mScroller!!.currY)
            invalidate()
        }
        else if (isStarScroll){
            isStarScroll = false
            if (mDeleteBtnState == 1)
                mDeleteBtnState =0
            if (mDeleteBtnState == 2)
                mDeleteBtnState = 3
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker!!.recycle()
        super.onDetachedFromWindow()
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        isDragging = state == SCROLL_STATE_DRAGGING
    }

    fun setOnclickListener(listener:((position:Int)->Unit)){

        mListenerDelete = listener

    }

}