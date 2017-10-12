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
import dinson.customview._global.BaseActivity;
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
        GlideUtils.setImage(this, hear_url, (ImageView) findViewById(R.id.fiv_heard));

        String imgUrl = "http://ondlsj2sn.bkt.clouddn.com/FoPzP9JbDTqxMlhWCRvxPUo24IRn.webp";
        GlideUtils.setCircleImage(this, imgUrl, (CircleImageView) findViewById(R.id.cv_circleview));

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Quan Zhou\n");

        SpannableStringBuilder builder = new SpannableStringBuilder("Eastern Asia culture");
        builder.setSpan(new RelativeSizeSpan(0.56f), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTitle.append(builder);

        TextView tvContent = (TextView) findViewById(R.id.tvContent);
        String qzStr = FileUtils.getTextFromAssets(this, "QuanZhou");

        tvContent.setText(Html.fromHtml(qzStr));
    }
}
