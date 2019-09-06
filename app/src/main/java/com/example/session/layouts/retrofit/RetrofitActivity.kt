package com.example.session.layouts.retrofit

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.session.R
import com.example.session.databinding.RetrofitActivityImpl
import com.example.session.http.RequestServiceImpl
import kotlinx.android.synthetic.main.activity_retrofit.*

class RetrofitActivity : AppCompatActivity() {

    val serviceImpl = RequestServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<RetrofitActivityImpl>(this, R.layout.activity_retrofit)
        binding.eventHandler = EventHandler()
    }

    inner class EventHandler{
        fun onClick(view: View) {
            serviceImpl.getFeeds()
        }
    }
}
