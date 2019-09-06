package com.example.session.layouts

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.res.TypedArray
import android.graphics.drawable.Animatable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import com.example.session.R
import kotlinx.android.synthetic.main.activity_animation.*

class AnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        /***
         * repeatCount与interpolator己经不能在xml里面配置了
         * repeatCount<0 表示无限，
         */
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.tween)
        animation.repeatCount = Animation.INFINITE // tween动画虽然设置了无限循环，但是没用，需要在结束时再次开始，属性动画可以无限循环
        animation.setInterpolator(this, android.R.anim.linear_interpolator)
        animation.repeatMode = Animation.RESTART

        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                val animation: Animation = AnimationUtils.loadAnimation(this@AnimationActivity, R.anim.tween)
                animation.setAnimationListener(this)
                tweenAnimation.startAnimation(animation)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        tweenAnimation.startAnimation(animation)

        (frameAnimation.background as Animatable).start()

        /**
         * 加载属性动画xml时要用AnimatorInflater
         */
        AnimatorInflater.loadAnimator(this, R.animator.property).apply {
            setTarget(propertyAnimation)
            start()
        }
    }

}
