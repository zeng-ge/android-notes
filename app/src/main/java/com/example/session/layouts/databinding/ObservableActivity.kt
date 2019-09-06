package com.example.session.layouts.databinding

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.session.R
import com.example.session.databinding.ObservableActivityImpl
import com.example.session.models.User
import kotlinx.android.synthetic.main.activity_observable.*

class ObservableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_observable)
        val binding: ObservableActivityImpl = DataBindingUtil.setContentView<ObservableActivityImpl>(this, R.layout.activity_observable)
        val product = Product("iphone", "apple cell phone")
        val order = Order("hello phone")
        var user = User("sky", 20, "xi'an")
        binding.product = product
        binding.order = order
        binding.user1 = user

        binding.eventHandler = EventHandler()
        textView.setOnClickListener {
            product.details = "full screen"
            product.name = "mix2s"

            order.name.set("hello world")
            user.name = "abc"
        }

        var messageQueue: MessageQueue

        Thread {
            messageQueue = MessageQueue()
        }.start()
    }

    inner class EventHandler{
        fun onClick(view: View) {
            Log.i("ObservableActivity", "click view")
            Toast.makeText(this@ObservableActivity, "click view handler", Toast.LENGTH_SHORT).show()
        }
    }
}
