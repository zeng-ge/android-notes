package com.example.session

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/***
 * 可以将没有内容的Fragment添加到Activity里面，用它来监控生命周期
 * livingData里面的ReportFragment就是这种思路
 *
 * 与有容器的Fragement的区别在于：
 * 查找Fragment要用findFragmentByTag
 * 添加Fragment要用add(EmptyFragment(), Tag_NAME)//有容器的Fragment分别是findFragmentById, add(container_id, Fragment())
 */
class EmptyFragmentActivity: AppCompatActivity() {
    final val tag:String = "empty-fragment";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_fragment)
        val fragemnt: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        fragemnt ?: null.let{
            supportFragmentManager.beginTransaction().add(EmptyFragment(), tag).commit()
        }
    }
}

/**
 * Fragment的生命周期分两批：
 * Activity.onStart
 *  onAttach                state 0
 *  onCreate                state 0
 *  onCreateView            state 1
 *  onActivityCreated       state 1
 *  上面4个方法在FragmentManagerImpl.moveToState方法里面依次调用
 *  onStart
 *  上面这几个事件都是在FragmentActivity的onStart触发时调用
 * 其它事件都有对应的转发
 * 如onStop, onPause, onResume
 *
 * Fragment可以看作是具体生命周期的组件
 */
class EmptyFragment: Fragment() {

    val logTag: String = "EmptyFragment"

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.i(logTag, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(logTag, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(logTag,"onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(logTag,"onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i(logTag,"onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(logTag,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(logTag,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(logTag,"onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(logTag,"onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(logTag,"onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(logTag,"onDetach")
    }
}