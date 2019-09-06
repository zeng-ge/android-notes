package com.example.session.layouts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.example.session.R
import kotlinx.android.synthetic.main.activity_inflator.*

class InflatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inflator)

        /**
         * 两个参数时：inflate(resource, root, root != null) 第三个参数会根据是否有root默认设定为true(有root)或false(无root)
         * 完整版的inflate是三个参数：layout文件，父组件，是否添加到父组件
         *
         * 1) resource, null, true  无意义
         * 2) resource, null, false 返回根据layout创建的组件
         *
         * 3) resource, parent, true    将layout生成的组件添加到parent里面，返回parent
         * 4) resource, parent, false   返回组件
         * (root == null || !attachToRoot)时返回组件
         * 调用parmas = parent.generateLayoutParams(attrs)为组件生成布局属性temp.setLayoutParams(params)
         *
         * 总结：有意义的调用有三种
         * 1) inflate(layout, null)这种返回创建的组件，但是布局属性无效
         * 2) inflate(layout, parent, true)创建组件添加进parent，返回parent
         * 3) inflate(layout, parent, false)用parent来计算组件的布局属性
         * */

        //第一种用法，没有计算布局属性，宽高不确定
        LayoutInflater.from(this).inflate(R.layout.inflator_button, null).let {
            (linearLayout as ViewGroup).addView(it)
            Log.i("InflatorActivity", it.toString())
        }

        //直接就加进parent了，正常布局
        LayoutInflater.from(this).inflate(R.layout.inflator_button, linearLayout, true).let {
            Log.i("InflatorActivity", it.toString())
        }

        //第三种用法, 由于parent计算了布局属性，所以有宽高，第三种更灵活(下面将button添加到了另外一个容器里面，布局属性也是生效的，即宽高己定)
        LayoutInflater.from(this).inflate(R.layout.inflator_button, linearLayout, false).let {
            (bottomLinearLayout as ViewGroup).addView(it as Button)
            Log.i("InflatorActivity", it.toString())
        }
    }
}
