package com.example.session.utils

import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView

object TextBind{
    @JvmStatic
    @BindingAdapter("text")
    fun text(view: View, text: String): Unit{
        (view as TextView).text = text
    }
}