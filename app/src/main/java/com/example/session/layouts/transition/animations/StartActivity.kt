package com.example.session.layouts.transition.animations

import android.animation.*
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionInflater
import android.util.Pair
import android.view.Gravity
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.example.session.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS) //必须在setContentView之前调用

        setContentView(R.layout.activity_start)

        normalBtn.setOnClickListener {
            startActivity(Intent(this@StartActivity, EndActivity::class.java).also {
                it.putExtra("type", "normal")
            })
            /***
             * 标准的进出场
             * 两个参数分别是进场与出场动画, animation
             * 这里是用两个translate来做平移，入场是x从100%到0%，出场是x从0%到-100%
             *
             * 缺点：前进后退时都得这样调
             * overridePendingTransition必需紧挨着startActivity()或者finish()函数之后调用；
             */
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

        explodeBtn.setOnClickListener {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                //退出, 进入EndActivity时StartActivity是退出
                window.exitTransition = Explode().also {
                    it.duration = 5000
                }
                //返回
                window.reenterTransition = Explode().also {
                    it.duration = 10000
                }
                val options = ActivityOptions.makeSceneTransitionAnimation(this)
                startActivity(Intent(this@StartActivity, EndActivity::class.java).also {
                    it.putExtra("type", "explode")
                }, options.toBundle())
            }
        }

        slideBtn.setOnClickListener {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                window.exitTransition = Slide().also {
                    it.duration = 5000
                    it.slideEdge = Gravity.LEFT //控制activity进入的方向
                }
                window.reenterTransition = Slide().also {
                    it.duration = 10000
                    it.slideEdge = Gravity.LEFT
                }
                val options = ActivityOptions.makeSceneTransitionAnimation(this)
                startActivity(Intent(this@StartActivity, EndActivity::class.java).also {
                    it.putExtra("type", "slide")
                }, options.toBundle())
            }
        }

        fadeBtn.setOnClickListener {
            //从配置文件加载transition，基本只是配duration,还不如直接创建对象直接
            val fade = TransitionInflater.from(this).inflateTransition(R.transition.explode)
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                window.exitTransition = fade //退出动画
                //返回时动画
                window.reenterTransition = Fade().also {
                    it.duration = 10000
                }
                val options = ActivityOptions.makeSceneTransitionAnimation(this)
                startActivity(Intent(this@StartActivity, EndActivity::class.java).also {
                    it.putExtra("type", "fade")
                }, options.toBundle())
            }
        }

        /***
         * shared element可以是任意元素，不一定要是图片，只不过图片看上去效果最明显
         * 小图放大时用这个效果特别好
         *
         * 从效果来看，相当于两个动画，activity本身的入场动画以及元素移动的动画，将时间延长后看的特别明显
         */
        shareElementBtn.setOnClickListener {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                startActivity(Intent(this@StartActivity, EndActivity::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(sharedBtn, "sharedImageView")).toBundle())
            }

        }

        circleBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val density = resources.displayMetrics.density
                /***
                 * width, height, x,y的单位都是象素
                 */
                val width: Int = circleBtn.width
                val height: Int = circleBtn.height
                val centerX: Int = (width/2).toInt()
                val centerY: Int = (height/2).toInt()
                var radius: Float = Math.hypot(width.toDouble(), height.toDouble()).toFloat()/2
                /***
                 * centerX是相对于动画组件的
                 * 0, 0表示左上角
                 * centerX与centerY取的是象素值
                 *
                 */
                ViewAnimationUtils.createCircularReveal(circleBtn,
                        centerX,
                        centerY,
                        radius, 0f).also {
                    it.duration = 2000
                    it.start()
                };
            }
        }

        /***
         * 属性动画可以无限循环
         * ValueAnimator虽然好用，但是需要人工来指定怎么应用属性值，对应配置文件中的animator
         * ObjectAnimator是ValueAnimator的进化版，它可以指定属性，它对应animator配置文件中的objectAnimator
         * ValueAnimator设置PropertyValuesHolder无效
         */
        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.duration = 2000
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = AccelerateDecelerateInterpolator()
//        animator.setValues(PropertyValuesHolder.ofFloat("rotationY", 0f, 360f))
        animator.addUpdateListener(object: ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                val value: Float = animation?.animatedValue as Float
                sharedBtn.rotationY = value
            }
        })
        animator.start()

        /***
         * 相对于ValueAnimator而言，可以直接配置属性
         */
        val objectAnimator = ObjectAnimator.ofFloat(sharedCopyBtn, "rotationY", 0f, 360f)
        objectAnimator.duration = 2000
        objectAnimator.repeatMode = ObjectAnimator.RESTART
        objectAnimator.repeatCount = ObjectAnimator.INFINITE
        objectAnimator.interpolator = LinearInterpolator()
//        objectAnimator.addUpdateListener(), 当然它也可以配置监听器
        /***
         * ObjectAnimator可以将值应用于多个属性改变上
         * 对应的处理对象就是PropertyValuesHolder, 在配置文件中作为objectAnimator的子元素propertyValuesHolder
         * 配了propertyValuesHolder后，objectAnimator的propertyName就失效了
         */
        objectAnimator.setValues(
            PropertyValuesHolder.ofFloat("translationX", 0f, 40f),
            PropertyValuesHolder.ofFloat("translationY", 0f, 40f),
            PropertyValuesHolder.ofFloat("rotationY", 0f, 360f)
        )
        objectAnimator.start()

        /***
         * AnimatorSet不能repeat，所以要用监听器来让其一直运行
         */
        val animatorSet = AnimatorSet().also {
            it.duration = 3000
        }
        animatorSet
            .play(ObjectAnimator.ofFloat(sharedCopyBtn2, "translationX", 0f, 200f))
            .after(ObjectAnimator.ofFloat(sharedCopyBtn2, "translationX", 0f, -200f))
        animatorSet.addListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                //让其一直运行
                animatorSet.start()
            }

            override fun onAnimationRepeat(animation: Animator?) {


            }

            override fun onAnimationCancel(animation: Animator?) {

            }
        })
        animatorSet.setTarget(sharedCopyBtn2)
        animatorSet.start()

    }
}
