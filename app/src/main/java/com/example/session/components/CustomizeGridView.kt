package com.example.session.components

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.example.session.R

class CustomizeGridView constructor(context: Context, attrs: AttributeSet?): ViewGroup(context, attrs){

    var columnCount: Int = 0
    var columnSpan: Int = 0
    var rowSpan: Int = 0

    init {
        initAttributes(attrs)
    }

    fun initAttributes(attrs: AttributeSet?): Unit{
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomizeGridView)
        columnCount = typedArray.getInt(R.styleable.CustomizeGridView_columnCount, 0)
        columnSpan = typedArray.getLayoutDimension(R.styleable.CustomizeGridView_columnSpan, 0)
        rowSpan = typedArray.getLayoutDimension(R.styleable.CustomizeGridView_rowSpan, 0)
        typedArray.recycle()
    }

    /**
     * 自算自己的大小，以及给子元素一个参考大小
     * 这里其实要根据mode来计算宽度与高度
     * 实际上宽度肯定是要与parent一样的，不然子元素的格子无法计算
     * 高度如果是wrap-content就需要累计子元素的高度，如果是match-parent就直接用父元素给定的，但是有个问题，比父元素大怎么办？
     *
     * 对于子元素：
     * 虽然给了参考大小，如果子元素是wrap-content，它可能比参考值小得多，如果要居中之类的就得在onLayout时计算其精确位置
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val availableWidth = width - paddingLeft - paddingRight
        val itemWidth = (availableWidth - (columnCount - 1) * columnSpan)/columnCount
        val rowNum = if (childCount%columnCount == 0) childCount/columnCount else childCount/columnCount + 1;
        val height = itemWidth * rowNum + rowSpan * (rowNum - 1)
        val itemSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY)
        if(childCount == 0){
          setMeasuredDimension(0, 0)
            return
        }

        /***
         * 这里其实是将ViewGroup里面的measureChildren做的事干了
         * measureChildren与getChildMeasureSpec很重要
         */
        for(index: Int in 0 until childCount) {
            val child = getChildAt(index)
            //让child计算自己的大小
            measureChild(child, itemSpec, itemSpec)
        }
        //设置自身的宽高
        setMeasuredDimension(width, height)
    }

    /***
     * 计算每个child的位置
     *
     * 如果子元素是wrap-content, 这里的right与bottom就需要跟根据onMeasure里面的itemWidth来设置位置
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for(index: Int in 0 until  childCount) {
            var position = index + 1
            val child = getChildAt(index)
            val childRow = if (position % columnCount == 0) position/columnCount else position/columnCount + 1
            val childColumn = if (position%columnCount == 0) columnCount else position % columnCount
            val left = paddingLeft + (childColumn - 1) * (columnSpan + child.measuredWidth)
            var top = paddingTop + (childRow -1) * (rowSpan + child.measuredWidth)
            child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
        }
    }
}