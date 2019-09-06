package com.example.session.layouts.databinding

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.example.session.BR
import kotlin.properties.Delegates

open class Product constructor(var productName: String, var details: String): BaseObservable(){
//    var name: String
//        @Bindable get() = productName
//        set(value) {
//            productName = value
//            notifyPropertyChanged(BR.name)
//        }
    /***
     * 升级成属性代理
     * 编译结果
     * @Bindable
        @NotNull
        public final String getName() {
        return (String)this.name$delegate.getValue(this, $$delegatedProperties[0]);
        }
     */

    var name: String by Delegates.observable(productName) { prop, old, new ->
        notifyPropertyChanged(BR.name)
    }
        @Bindable get //相当于在var name上面加@get:Bindable，编译出来是一样的

};