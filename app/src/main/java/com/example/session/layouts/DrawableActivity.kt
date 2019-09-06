package com.example.session.layouts

import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ScaleDrawable
import android.graphics.drawable.TransitionDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.session.R
import kotlinx.android.synthetic.main.activity_drawable.*

class DrawableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable)

        levelListBtn.background.setLevel(2)

        (transitionBtn.background as TransitionDrawable).startTransition(2000)

        /***
         * clip drawable默认裁剪级别是0,即完全裁剪，10000为不裁剪
         */
        (clipBtn.background as ClipDrawable).setLevel(4000)
//        (scaleBtn.background as ScaleDrawable).setLevel(10000)
    }
}
