package dinson.customview.utils;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import dinson.customview.R;


/**
 * glide工具类
 */
public class GlideUtils {

    public static RequestOptions getDefaultOptions() {
        return new RequestOptions()
                .placeholder(R.drawable.def_img)
                .error(R.drawable.def_img)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop().dontAnimate();
    }

    public static void setCircleImage(Context context, String url, ImageView view) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.def_img_round_holder)
                .error(R.drawable.def_img_round_error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .circleCrop().dontAnimate();
        Glide.with(context).load(url).apply(options).into(view);
    }

    public static void setImage(Context context, String url, ImageView view) {
        if (url.endsWith(".svg") || url.endsWith(".SVG")) {
            setSvgImage(context, url, view);
            return;
        }
        Glide.with(context).load(url).apply(getDefaultOptions()) .into(view);
    }

    private static void setSvgImage(Context context, String url, ImageView view) {
        Glide.with(context)
                .as(PictureDrawable.class)
                .error(R.drawable.def_img).load(url).into(view);
    }


    /*Glide储存本地文件*/
            /*Single.just(mHeadData[position].data.hp_img_url)
                .map { s ->
                    Glide.with(this@MainActivity).downloadOnly().load(s).downloadOnly(Target.SIZE_ORIGINAL,
                        Target.SIZE_ORIGINAL).get().path
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { path ->
                    debug("CurrentItem:" + position + " img:" + mHeadData[position].data.hp_img_url + "  imgPath:" + path)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, view, "dailyPic")
                    DailyPicActivity.start(this@MainActivity, mHeadData[position].data, path, options)
                }*/
}
