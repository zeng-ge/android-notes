package com.example.session.layouts.databinding

import android.databinding.ObservableField

class Order(_name: String) {
    lateinit var name: ObservableField<String>
    init{
        name = ObservableField<String>(_name)
    }
}