package com.example.session

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Choreographer
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_choreographer.*

class ChoreographerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choreographer)

        val choreographer = Choreographer.getInstance()

        val attachStateBtn: Button = Button(this)
        attachStateBtn.id = View.generateViewId()
        attachStateBtn.text = "attach state button"

        attachStateBtn.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener{
            override fun onViewDetachedFromWindow(v: View?) {
                Log.i("ChoreographerActivity", "button detach from window")
            }

            override fun onViewAttachedToWindow(v: View?) {
                Log.i("ChoreographerActivity", "button attach to window")
            }
        })

        attachStateBtn.setOnClickListener {
            Log.i("ChoreographerActivity", "click attach state button")
        }

        // constraint layout想动态添加元素需要添加约束，同LayoutParams有点不同
        val cl = ConstraintSet()
        /***
         * connect相当于layout_constraintTop_toTopOf
         * 第一个id表明目标元素, Constraint.TOP相当于layout_constraintTop
         * 第二个ID表明相对于谁， Constraint.BOTTOM相当于toBottomOf
         */
        cl.connect(attachStateBtn.id, ConstraintSet.TOP, R.id.postFrameBtn, ConstraintSet.BOTTOM)
        cl.connect(attachStateBtn.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        cl.connect(attachStateBtn.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        cl.constrainWidth(attachStateBtn.id, ConstraintSet.WRAP_CONTENT)
        cl.constrainHeight(attachStateBtn.id, ConstraintSet.WRAP_CONTENT)

        postFrameBtn.setOnClickListener {

            if (!attachStateBtn.isAttachedToWindow()) {
                choreographerContainer.addView(attachStateBtn)
                //将rule应用到constraintLayout上面
                cl.applyTo(choreographerContainer)
            } else {
                choreographerContainer.removeView(attachStateBtn);
            }

            Log.i("ChoreographerActivity", "click post frame button")

            // 事件在下一帧渲染时会触发
            choreographer.postFrameCallback {
                Log.i("ChoreographerActivity", "trigger post frame callback")
            }
        }

    }
}