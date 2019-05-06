package com.yuong.wanandroidkotlin.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText
import com.yuong.wanandroidkotlin.R


@SuppressLint("AppCompatCustomView")
class MyClearEditText(private val mContext: Context, attrs: AttributeSet?, defStyleAttr: Int) : EditText(mContext, attrs, defStyleAttr), TextWatcher {

    private var leftIcon: Drawable? = null
    private var icDelete: Drawable? = null

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.editTextStyle) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MyClearEditText)

        // 解析集合中的属性circle_color属性
        // 该属性的id为:R.styleable.CircleView_circle_color
        // 将解析的属性传入到画圆的画笔颜色变量当中（本质上是自定义画圆画笔的颜色）
        // 第二个参数是默认设置颜色（即无指定circle_color情况下使用）
        leftIcon = ta.getDrawable(R.styleable.MyClearEditText_leftIcon)

        // 解析后释放资源
        ta.recycle()
        init()
    }

    init {
        init()
    }

    private fun init() {
        // getCompoundDrawables() Returns drawables for the left(0), top(1), right(2) and bottom(3)
        //        leftIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_user);
        icDelete = ContextCompat.getDrawable(mContext, R.drawable.ic_ios_close_outline)

        setImage()

    }

    /**
     * 我们无法直接给EditText设置点击事件，只能通过按下的位置来模拟clear点击事件
     * 当我们按下的位置在图标包括图标到控件右边的间距范围内均算有效
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (icDelete != null && event.action == MotionEvent.ACTION_UP) {
            // 起始位置
            val start = width - totalPaddingRight + paddingRight
            // 结束位置
            val end = width
            val available = event.x > start && event.x < end
            if (available) {
                this.setText("")
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setImage() {
        /*
            setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
            意思是设置Drawable显示在text的左、上、右、下位置。
         */
        if (length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, icDelete, null)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, end: Int, after: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, end: Int, after: Int) {
        setImage()
    }

    override fun afterTextChanged(editable: Editable) {

    }

    /**
     * 设置晃动动画
     */
    fun setShakeAnimation() {
        this.startAnimation(shakeAnimation(5))
    }

    companion object {


        /**
         * 晃动动画
         *
         * @param counts 1秒钟晃动多少下
         * @return
         */
        fun shakeAnimation(counts: Int): Animation {
            val translateAnimation = TranslateAnimation(0f, 10f, 0f, 0f)
            translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
            translateAnimation.duration = 1000
            return translateAnimation
        }
    }

}
