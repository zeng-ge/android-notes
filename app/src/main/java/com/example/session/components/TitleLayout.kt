package com.example.session.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

import com.example.session.R
import kotlinx.android.synthetic.main.title_layout.view.*

class TitleLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title_layout, this)
        /***
         * 最后一个参数是函数的话，用lamda表达式代替, lt是第一个参数，
         * 如果多个参数：
         * {a, b, c ->
         *
         * }
         */
        title.setOnClickListener{
            println(it.toString())
        }
    }
}
