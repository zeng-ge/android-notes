package com.example.session.layouts.events

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.*
import com.example.session.R
import kotlinx.android.synthetic.main.activity_drag.*

class DragActivity : AppCompatActivity() {

    val gestureDetector = GestureDetector(GerryGestureDetector())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)

        dragButton.setOnTouchListener(object: View.OnTouchListener{
            lateinit var downPointer: Point
            /**
             * onTouch返回值
             * true     自己就处理完了，不调用父类的onTouchEvent方法
             * false    自己只是处理完一部分，以Button为例，父类为TextView，返回false时，会接着调用TextView.onTouchEvent,
             *          TextView.onTouchEvent内部甚至还会调用super.onTouchEvent，即View.onTouchEvent
             *
             * invalidate与requestLayout的区别：
             * invalidate不会触发onMeasure与onLayout
             * requestLayout会触发onMeasure与onLayout
             */
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                Log.i("DragActivity", "button postion, top: ${v?.top} left: ${v?.left}")
                Log.i("DragActivity", "event position, x: ${event?.x} left: ${event?.y}")
                when(event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downPointer = Point(event.x.toInt(), event.y.toInt())
                        Log.i("DragActivity", "ACTION_DOWN")
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.i("DragActivity", "ACTION_UP")
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val offsetX = event.x.toInt() - downPointer.x
                        val offsetY = event.y.toInt() - downPointer.y
                        v?.also {
                            val left = v.left.toInt() + offsetX + 200
                            val top = v.top.toInt() + offsetY + 200
                            //点击事件后会执行doTraversal => performTraversals来进行重绘，不调用it.layout也会触发，只不过调用后大小就变了
                            it.layout(left, top, left + v.width, top + v.height)
                            dragButton2.layout(dragButton2.left, dragButton2.top, dragButton2.left + 100, dragButton2.bottom)
                            // requestLayout会倒致至上而下的performTraversals，
                            // 这里drage button调用了requestLayout，它的onMeasure与onLayout都会触发，而drag button2只会触发onLayout
                            // requestLayout 会将标志位PFLAG_FORCE_LAYOUT打开，
//                            it.requestLayout()
                            it.requestLayout()
                        }
                        Log.i("DragActivity", "ACTION_MOVE")
                    }
                }
                return false
            }
        })


        dragContainer.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.i("DragActivity", "ViewGroup: ACTION_DOWN")
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.i("DragActivity", "ViewGroup: ACTION_UP")
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Log.i("DragActivity", "ViewGroup: ACTION_MOVE")
                    }
                }
                return false
            }
        })

        guestureDetectorButton.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return gestureDetector.onTouchEvent(event)
            }
        })
    }

    class GerryGestureDetector : GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
        //按下并停留一会,离开太快不会触发
        override fun onShowPress(e: MotionEvent?) {
            Log.i("DragActivity", "GestureDetector: onShowPress")
        }

        //按下并弹起，按的时候太长会触发LongPress，再弹起时不会触发
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.i("DragActivity", "GestureDetector: onSingleTapUp")
            return true
        }

        //按下就触发
        override fun onDown(e: MotionEvent?): Boolean {
            Log.i("DragActivity", "GestureDetector: onDown")
            return true
        }

        //down => scroll => fling，快速的拉动手指并提起
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Log.i("DragActivity", "GestureDetector: onFling")
            return true
        }

        //滚动
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Log.i("DragActivity", "GestureDetector: onScroll")
            return true
        }

        //长按触发
        override fun onLongPress(e: MotionEvent?) {
            Log.i("DragActivity", "GestureDetector: onLongPress")
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.i("DragActivity", "GestureDetector: onDoubleTap")
            return true
        }

        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            Log.i("DragActivity", "GestureDetector: onDoubleTapEvent")
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            Log.i("DragActivity", "GestureDetector: onSingleTapConfirmed")
            return true
        }
    }
}
