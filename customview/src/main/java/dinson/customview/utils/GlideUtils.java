package dinson.customview.utils;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import dinson.customview.R;
import dinson.customview.utils.svg.GlideApp;


/**
 * glide工具类
 */
public class GlideUtils {

    public static void setCircleImage(Context context, String url, ImageView view) {
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.def_img_round)
                .circleCrop();
        Glide.with(context).load(url).apply(requestOptions).into(view);
    }

    public static void setImage(Context context, String url, ImageView view) {

        if (url.endsWith(".svg") || url.endsWith(".SVG")) {
            setSvgImage(context, url, view);
            return;
        }

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.default_image)
                .error(R.drawable.default_image).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate();
        Glide.with(context).load(url).apply(requestOptions).into(view);
    }

    public static void setSvgImage(Context context, String url, ImageView view) {
        RequestBuilder<PictureDrawable> requestBuilder = GlideApp.with(context)
                .as(PictureDrawable.class)
                .error(R.mipmap.ic_launcher) ;
        requestBuilder.load(url).into(view);
//        RequestBuilder<PictureDrawable> as = Glide.with(context).as(PictureDrawable.class);
//        as.load(url).into(view);
    }
}
