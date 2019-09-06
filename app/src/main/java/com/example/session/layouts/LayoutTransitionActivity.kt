package com.example.session.layouts

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.LinearLayout
import com.example.session.R
import kotlinx.android.synthetic.main.activity_layout_transition.*

class LayoutTransitionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_transition)

        var index = 1;
        addItem.setOnClickListener {
            val button = Button(this@LayoutTransitionActivity)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            button.layoutParams = params
            button.text = "Button $index"
            index++
            buttonContainer.addView(button)
            buttonEnterContainer.visibility = View.VISIBLE
        }

        removeItem.setOnClickListener {
            if(buttonContainer.childCount > 0) {
                buttonContainer.removeViewAt(0)
            }
            buttonEnterContainer.visibility = View.GONE
        }


        initLayoutTransition()

        //相当于在 xml中配置android:layoutAnimation
//        buttonEnterContainer.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim)

    }

    fun initLayoutTransition(): Unit {
        buttonContainer.layoutTransition = LayoutTransition().also {

            val appearing = it.getDuration(LayoutTransition.APPEARING)
            val disappearing = it.getDuration(LayoutTransition.DISAPPEARING)
            val change_appearing = it.getDuration(LayoutTransition.CHANGE_APPEARING)
            val change_disappearing = it.getDuration(LayoutTransition.CHANGE_DISAPPEARING)


            /**
             * 作用于新增view上
             */
            it.setAnimator(LayoutTransition.APPEARING,
                ObjectAnimator.ofFloat(null, "rotationY", -90f, 0f).also {
                it.duration = appearing
            })
            /***
             * 作用于隐藏或消失的view上
             */
            it.setAnimator(LayoutTransition.DISAPPEARING,
                ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).also {
                it.duration = disappearing
            })

            it.setStagger(LayoutTransition.CHANGE_APPEARING, 100)
            it.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 100)


            /***
             * 当新增view时，容器的动画
             */
            it.setAnimator(LayoutTransition.CHANGE_APPEARING,
                ObjectAnimator.ofInt(null, "left", 0, 100).also {
                    it.duration = change_appearing
                })

            /***
             * 当删除时，容器的动画
             */
            it.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
                    ObjectAnimator.ofInt(null, "right", 0, 100).also {
                        it.duration = change_disappearing
                    })

        }
    }
}
