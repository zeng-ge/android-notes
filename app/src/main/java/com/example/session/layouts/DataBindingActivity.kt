package com.example.session.layouts

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.session.R
import com.example.session.databinding.UserBinding
import com.example.session.models.User
import com.example.session.utils.StringUtils
import kotlinx.android.synthetic.main.activity_data_binding.*

class DataBindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_data_binding)
        //用DataBindingUtil.setContentView取代Activity.setContentView
        val binding: UserBinding = DataBindingUtil.setContentView<UserBinding>(this, R.layout.activity_data_binding)
        binding.user = User("sky", 20, "xi'an")

        button1.setOnClickListener {
            //修改user后数据直接更新, binding.user.name = "abc"会报错，只能整体一次情给值
            binding.user = User("11223", 20, "xi'an")
        }
    }
}
