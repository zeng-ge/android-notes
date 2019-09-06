package com.example.session.layouts.databinding

import kotlin.reflect.KProperty

/***
 * 相当于代理模式
 * 假定MutableList的add方法需要增强，但是又不能改源码，怎么办？
 * 传统的代理模式是这样干的：
 * class MyMutableList extends MutableListInterface{//代理类需要实现相同的接口
 * //这种写法有个很弱智的地方，接口里面定义了大量的方法，需要实现一大堆，太麻烦了，而kotlin代理可以自动生成没有实现的方法
 *  public boolean add(){ //重写这个方法
 *      new MutableList().add()//调用己有的MutableList的方法，然后在前面或后面添加新的功能
 *      println("proxy add")
 *  }
 * }
 */
class MutableListDelegate<T>(val myCollection: MutableList<T>): MutableList<T> by myCollection{
    override fun add(element: T): Boolean {
        println("proxy add")
        return myCollection.add(element)
    }
}

class People(var _name: String) {
    val addressDelegate: Address = Address()
    var name: String
        get() = _name
        set(value) {
            _name = value
        }
    //属性代理的写法：
    var address: String by addressDelegate

    val lazyValue: String by lazy {
        println("computed!")
        "Hello"
    }
}

/***
 * 属性代理要实现getValue与setValue，方法必须加operator，不然报warning，不加编译出来的结果是一样的
 * 相当于：
 * var address: String
 *      get() = delegate.getValue()
 *      setValue(value){
 *          delegate.setValue(value)
 *      }
 */
class Address {
    var _value: String = ""

    operator fun getValue(thisRef: People, property: KProperty<*>): String{
        return _value
    }

    operator fun setValue(thisRef: People, property: KProperty<*>, str: String): Unit{
        _value = str
    }
}