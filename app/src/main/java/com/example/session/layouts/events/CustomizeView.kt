package com.example.session.layouts.events

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class CustomizeView(context: Context, attrs: AttributeSet): View(context, attrs){
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        Log.i("DragActivity", "CustomizeView")
        return false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i("DragActivity", "CustomizeView onMeasure")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.i("DragActivity", "CustomizeView onLayout")
    }
}