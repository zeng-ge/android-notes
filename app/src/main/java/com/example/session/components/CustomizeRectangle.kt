package com.example.session.components

import android.content.Context
import android.content.res.AssetManager
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.example.session.R
import kotlinx.android.synthetic.main.activity_customize.view.*
import java.io.File
import java.nio.charset.Charset
import java.util.*

class CustomizeRectangle constructor(context: Context, attrs: AttributeSet?): View(context, attrs){

    init{
        //
        /***
         * 最终是调用theme的obtainStyledAttributes,完整的4个参数如下（少的会用null或0代替）：
         *  AttributeSet set,
            @StyleableRes int[] attrs,  最重要的参数是它，即declare-styleable里面定义的属性集
            @AttrRes int defStyleAttr,
            @StyleRes int defStyleRes
        相当于context.theme.obtainStyledAttributes(attrs, R.styleable.CustomizeRectangle, 0, 0)

        context.obtainStyledAttributes(R.styleable.CustomizeRectangle)只用一个参数得到的TypedArray的mIndices为0,取不到值没有任何义意
         所有资源的获取最终是依赖ResourcesImpl => AssetManager(大部分方法都是native的) 来获取

         */

        /***
         * 取值为两大类(都可以通过context.obtainStyledAttributes来获取)：
         * 1）自定义的样式属性
         * obtainStyledAttributes(attrs, R.styleable.CustomizeRectangle)
         * 2）android本身定义好的样式属性
         * obtainStyledAttributes(attrs, ofIntArray(android.R.attr.layout_width))//可以取多个值，按index来
         * 不能想当然的以为可以obtainStyledAttributes(attrs, ofIntArray(R.styleable.CustomizeRectangle_fillColor))，这个fillColor只是个index，不是ID
         *
         * 要想通过第二种方式取自定义属性值要这样干：
         * <attr name="myColor" format="color">
         * <declare-styleable name="CustomizeRectangle"><attr name="myColor"></attr></declare-styleable>
         * 这样可以通过attr引用到myColor, 就可以用R.attr.myColor来作为resourceId来取它的值了,千万不能用R.styleable.CustomizeRectangle_myColor作为ID来取
         */

        /***
         * TypedArray内部的mdata存放的是一组index,
         * 比如这里的
         * R.styleable.CustomizeRectangle_fillColor对应的是mData[0] = 29，29是Color类型，接着取出mData[1]作为值，这里面的值存放的位置有偏移
         * R.styleable.CustomizeRectangle_text对应的是mData[6] = 3，这里的6 = 1*6//1为index, 6为TypedArray内部的一个因子 3为字符串类型
         * mData[6] = 3，这个3是表明是TypedValue.String, 接着在6 + 2即mData[8] > -1就取缓存, 找不到就取mData[7] = 15，用15作index将取到的字符串存在mString[15]
         * 搞的像hashMap算法，不同类型放在mdata不同地方，内部有各种映射，好烦
         *
         * 内部还有个mIndices，它记录了typedArray.indexCount
         * 如这里mIndices: [2, 0, 1]，第1位记录了总长度即要取属性的长度
         * 第从二位开始记录stylable资源对应的index
         *
         *
         * 总结：
         * mIndices记录要取属性的index及长度
         * mData内部存放index对应的类型，以及值，还有各种类型的index映射关系及缓存的index等，算法很复杂
         *
         * 只需要知道根据styleable资源对应的index能取到值就行了
         *
         */
        //用这个intArray代替R.styleable.CustomizeRectangle会报错，区别在于intArray为【0, 1]但R.styleable.CustomizeRectangle里面是个整数
        //但是在下面的when里面R.styleable.CustomizeRectangle_fillColor是个0，看来在编译过程中是干了什么事的
//        val intArray = intArrayOf(
//                R.styleable.CustomizeRectangle_fillColor, 0 不是资源ID，只是个index，所以取不到
//                R.styleable.CustomizeRectangle_text   1
//        )
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomizeRectangle)
        val color = typedArray.getColor(R.styleable.CustomizeRectangle_fillColor, 0)
        var text: String = typedArray.getString(R.styleable.CustomizeRectangle_text);
//        var layoutWidth: Int = typedArray.getLayoutDimension(2, 0)
        Log.i("CustomizeRectangle", "color: $color, text: $text")

        for(index in 0 until typedArray.indexCount) {
            val typedIndex: Int = typedArray.getIndex(index)
            when(typedIndex) {
                R.styleable.CustomizeRectangle_fillColor -> {//R.styleable.CustomizeRectangle_fillColor 在执行时结果是0，相当于定义它的styleable的顺序index
                    val color = typedArray.getColor(typedIndex, 0)
                    Log.i("CustomizeRectangle", "when -> color: $color, ${R.styleable.CustomizeRectangle_fillColor}")
                }
                R.styleable.CustomizeRectangle_text -> {//R.styleable.CustomizeRectangle_text 在执行时结果是1，相当于定义它的styleable的顺序index
                    val text = typedArray.getString(typedIndex)
                    Log.i("CustomizeRectangle", "when -> text: $text, ${R.styleable.CustomizeRectangle_text}")
                }

            }
        }
        Log.i("CustomizeRectangle", typedArray.indexCount.toString())
        typedArray.recycle()

        /**
         * AttributeSet也能得到长度，并根据index来遍历它，得到所有的属性名与值
         */
        for(index: Int in 0 until attrs!!.attributeCount){
            val name: String = attrs.getAttributeName(index)
            val value = attrs.getAttributeValue(index)
            Log.i("CustomizeRectangle", "attr name: ${name}, value: ${value}")
        }
        //根据属性名取属性值，怎么总是报错呢，加android的命名空间也报错
//        Log.i("CustomizeRectangle", "layout width: ${attrs.getAttributeValue(null, "layout_width")}")

        //想通过android.R.attr.layout_width来从TypedArray里面获取数据失败, inflate里面generateLayoutParams时会解析layout_width与layout_height


        /***
         * 用代码获取theme里面定义的样式属性：
         * 1) context.obtainStyledAttributes或context.theme.obtainStyledAttributes
         * 参数：R.attr或android.R.attr + .attrName 数组, 加不加android的结果都一样
         * 一次可以取多个
         * 2)context.theme.resolveAttribute
         * 参数：R.attr.attrName，TypedValue, true
         * 一次只能取一个
         *
         * 在android里面，颜色值范围从00000000-ffffffff,是一个有符号整数，所在在看值时是黑色
         */
        val styleTypedArray: TypedArray = context.obtainStyledAttributes(intArrayOf(R.attr.colorPrimary))
        val colorPrimary = styleTypedArray.getColor(0, resources.getColor(R.color.colorPrimary))
        val colorPrimary16 = String.format("%08x", colorPrimary)
        Log.i("CustomizeRectangle", "colorPrimary ${colorPrimary}, color: $colorPrimary16")
        styleTypedArray.recycle()

        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
        val color16 = String.format("%08x", typedValue.data)
        Log.i("CustomizeRectangle", "typedValue.resourceId ${typedValue.resourceId}, type: ${typedValue.type}, value: ${typedValue.data}, color: ${color16}")
        setBackgroundColor(typedValue.data)

        /***
         * layout_width也属于theme，所以可以通过android.R.attr.layout_width取到值
         */
        context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.layout_width, R.attr.myColor)).let {
            it.getLayoutDimension(0, 123).let {
                val backgroundColor = String.format("%08x", it)
                Log.i("CustomizeRectangle", "backgroundColor ${backgroundColor}")
            }
            it.getColor(1, 123).let {
                val myColor = String.format("%08x", it)
                Log.i("CustomizeRectangle", "myColor ${myColor}")
            }

            it.recycle()
        }


        /***
         * assetManager操作assets目录
         */
        resources.assets.list("").forEach {
            Log.i("CustomizeRectangle", it)
        }

        resources.assets.open("abc.txt").bufferedReader(Charset.defaultCharset()).readText().let {
            Log.i("CustomizeRectangle", "file content: $it")
        }

        //拿不到assets下的文件，这种方式只适合于webview, assets目录应该同class在一起
//        File("file:///android_asset/abc.txt").exists().also {
//            Log.i("CustomizeRectangle", "file content: $it")
//        }

        //取语言 1
        val locale = Locale.getDefault()
        Log.i("CustomizeRectangle", "locale: ${locale.language}")

        //2 从configuration里面取
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val locale2 = resources.configuration.locales.get(0)
            Log.i("CustomizeRectangle", "locale2: ${locale2.language}")
        } else {
            Log.i("CustomizeRectangle", "locale2: ${resources.configuration.locale}")
        }

        resources.getTextArray(R.array.array).forEach {
            Log.i("CustomizeRectangle", "array: $it")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.also{
            val paint = Paint()
            paint.color = Color.parseColor("#00ff00")


            it.drawRect(RectF(
                    this.paddingLeft.toFloat(),
                    this.paddingTop.toFloat(),
                    measuredWidth - this.paddingRight.toFloat(),
                    measuredHeight - this.paddingBottom.toFloat()), paint)

            paint.strokeWidth = 20f
            paint.color = Color.RED
            canvas.drawLines(floatArrayOf(10f, 10f, 100f, 10f, 100f, 50f, 10f, 50f), paint)
        }

    }

}