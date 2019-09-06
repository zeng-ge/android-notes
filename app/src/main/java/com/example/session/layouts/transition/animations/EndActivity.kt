package com.example.session.layouts.transition.animations

import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import com.example.session.R

class EndActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.activity_end)

        setTransition()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setTransition():Unit{
        val type = intent.getStringExtra("type")
        when(type) {
            "normal" -> {

            }
            "explode" -> {
                window.enterTransition = Explode().also {
                    it.duration = 2000
                }
                window.exitTransition = Explode().also {
                    it.duration = 2000
                }
            }
            "slide" -> {
                window.enterTransition = Slide().also {
                    it.duration = 1000
                    it.slideEdge = Gravity.TOP
                }
                window.exitTransition = Slide().also {
                    it.duration = 2000
                    it.slideEdge = Gravity.TOP
                }
            }
            "fade" -> {
                window.enterTransition = Fade().also {
                    it.duration = 2000
                }
                window.exitTransition = Fade().also {
                    it.duration = 2000
                }
            }
            else -> {
                window.enterTransition = Explode().also {
                    it.duration = 1000
                }
                window.exitTransition = Explode().also {
                    it.duration = 1000
                }
            }
        }
    }
}
