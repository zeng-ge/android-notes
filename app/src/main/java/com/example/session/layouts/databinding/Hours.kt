package com.example.session.layouts.databinding

inline class Hours(val number: Int){
    fun toMinutes() = number * 60
}