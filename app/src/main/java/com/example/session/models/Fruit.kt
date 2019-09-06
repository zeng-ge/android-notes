package com.example.session.models

class Fruit(val name:String)

fun Fruit.abc(str: String){
    println(str)
}

val Fruit.length: Int
    get() = name.length