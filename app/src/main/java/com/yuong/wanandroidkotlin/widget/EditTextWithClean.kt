package com.yuong.wanandroidkotlin.widget

/**带清除按钮的输入框
 * Created by Administrator on 2017/11/16.
 */

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText
import com.yuong.wanandroidkotlin.R


/**
 * @author admin
 */
@SuppressLint("AppCompatCustomView")
class EditTextWithClean : EditText {
    private var mClearDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @SuppressLint("NewApi")
    private fun init() {
        mClearDrawable = resources.getDrawable(R.drawable.ic_ios_close_outline)//图标
        this.setBackgroundColor(Color.WHITE)
        this.textSize = 15f
        this.background = resources.getDrawable(R.drawable.bg_shape_round_rect_wihte)
        //        this.setSingleLine();//密码不显示
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        setClearIconVisible(hasFocus() && text.length > 0)//text空时图标被隐藏
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        setClearIconVisible(focused && length() > 0)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val drawable = compoundDrawables[DRAWABLE_RIGHT]
                if (drawable != null && event.x <= width - paddingRight
                        && event.x >= width - paddingRight - drawable.bounds.width()) {//点击区域是图标
                    setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setClearIconVisible(visible: Boolean) {
        setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[DRAWABLE_LEFT], compoundDrawables[DRAWABLE_TOP],
                if (visible) mClearDrawable else null, compoundDrawables[DRAWABLE_BOTTOM])
    }

    companion object {

        private val DRAWABLE_LEFT = 0
        private val DRAWABLE_TOP = 1
        private val DRAWABLE_RIGHT = 2
        private val DRAWABLE_BOTTOM = 3
    }
}

