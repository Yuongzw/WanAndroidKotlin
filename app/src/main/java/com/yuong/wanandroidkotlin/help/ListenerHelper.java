package com.yuong.wanandroidkotlin.help;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yuong.wanandroidkotlin.R;


/**
 * 作者： 周旭 on 2017年10月17日 0017.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class ListenerHelper {
    /**
     * mv_vm xml 传入url 加载图片
     * imageUrl 为xml中 的命名
     *
     * @param iv  imageView
     * @param url 图片路径
     */
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView iv, String url) {
        Glide.with(iv.getContext())
                .load(url)
                .placeholder(R.drawable.default_project_img)
                .error(R.drawable.default_project_img)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(iv);
    }


    /**
     * mv_vm xml 设置 mipmap Resource
     *
     * @param iv    imageView
     * @param resId resource id
     */
    @BindingAdapter({"resId"})
    public static void loadMipmapResource(ImageView iv, int resId) {
        iv.setImageResource(resId);
    }
}
