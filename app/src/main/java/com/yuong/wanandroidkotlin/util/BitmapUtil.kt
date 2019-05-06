package com.yuong.wanandroidkotlin.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView


class BitmapUtil {
    companion object {
        fun recycleBackgroundBitMap(view: View?) {
            if (view != null) {
                val bd = view.background as BitmapDrawable
                rceycleBitmapDrawable(bd)
            }
        }

        fun recycleImageViewBitMap(imageView: ImageView?) {
            if (imageView != null) {
                val bd = imageView.drawable as BitmapDrawable
                rceycleBitmapDrawable(bd)
            }
        }

        private fun rceycleBitmapDrawable(bitmapDrawable: BitmapDrawable?) {
            var bitmapDrawable = bitmapDrawable
            if (bitmapDrawable != null) {
                val bitmap = bitmapDrawable.bitmap
                rceycleBitmap(bitmap)
            }
            bitmapDrawable = null
        }

        private fun rceycleBitmap(bitmap: Bitmap?) {
            var bitmap = bitmap
            if (bitmap != null && !bitmap.isRecycled) {
                bitmap.recycle()
                bitmap = null
            }
        }
    }

}