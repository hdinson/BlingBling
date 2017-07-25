package dinson.customview.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.TextView;

import dinson.customview.R;
import dinson.customview._globle.BaseActivity;
import dinson.customview.utils.FileUtils;
import dinson.customview.utils.GlideUtils;
import dinson.customview.weight.CircleImageView;

public class _003DiagonalLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__003_diagonal_layout);

        initUI();

    }

    private void initUI() {

        String hear_url = "http://ondlsj2sn.bkt.clouddn.com/FtT8Kk7HNbHE5FLf3U2dnXkOZtu7.jpeg";
        ImageView fiv_heard = (ImageView) findViewById(R.id.fiv_heard);
        GlideUtils.setImage(this, hear_url, fiv_heard);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Quan Zhou\n");

        SpannableStringBuilder builder = new SpannableStringBuilder("Eastern Asia culture");
        builder.setSpan(new RelativeSizeSpan(0.56f), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_title.append(builder);


        String imgUrl = "http://ondlsj2sn.bkt.clouddn.com/FsoyL8Ny12P4AUbz-4QP-URItTEW.png";
        CircleImageView cv_circleview = (CircleImageView) findViewById(R.id.cv_circleview);
        GlideUtils.setCircleImage(this, imgUrl, cv_circleview);

        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        String quanZhou = FileUtils.getTextFromAssets(this, "QuanZhou");
        tv_content.setText(Html.fromHtml(quanZhou));

    }
}
