package dinson.customview.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.utils.glide.BlurTransformation;
import dinson.customview.weight._001shimmerlayout.ShimmerFrameLayout;

public class _001ShimmerActivity extends BaseActivity {
    private ShimmerFrameLayout mShimmerViewContainer;
    private Button[] mPresetButtons;
    private int mCurrentPreset = -1;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__001_shimmer);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mTvTitle = findViewById(R.id.tvTitle);

        //设置背景（高斯模糊）
        RequestOptions options = new RequestOptions().dontAnimate()
                                                     .transform(new BlurTransformation(this, 23));
        // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
        ImageView img = findViewById(R.id.ivImg);
        Glide.with(this).load(R.drawable._001_bg).apply(options).into(img);

        mPresetButtons = new Button[]{findViewById(R.id.preset_button0), findViewById(
            R.id.preset_button1), findViewById(R.id.preset_button2), findViewById(
            R.id.preset_button3), findViewById(R.id.preset_button4),};
        for (int i = 0; i < mPresetButtons.length; i++) {
            final int preset = i;
            mPresetButtons[i].setOnClickListener(view -> selectPreset(preset));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectPreset(0);
    }

    @Override
    public void onResume() {
        super.onResume();

        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    /**
     * Select one of the shimmer animation presets.
     *
     * @param preset index of the shimmer animation preset
     */
    private void selectPreset(int preset) {
        if (mCurrentPreset == preset) {
            return;
        }
        if (mCurrentPreset >= 0) {
            mPresetButtons[mCurrentPreset].setBackgroundResource(R.color.preset_button_background);
        }
        mCurrentPreset = preset;
        mPresetButtons[mCurrentPreset].setBackgroundResource(R.color.preset_button_background_selected);

        // Save the state of the animation
        boolean isPlaying = mShimmerViewContainer.isAnimationStarted();

        // Reset all parameters of the shimmer animation
        mShimmerViewContainer.useDefaults();

        switch (preset) {
            default:
            case 0:
                // Default
                mTvTitle.setText("Default");
                break;
            case 1:
                // Slow and reverse
                mShimmerViewContainer.setDuration(5000);
                mShimmerViewContainer.setRepeatMode(ObjectAnimator.REVERSE);
                mTvTitle.setText("Slow and reverse");
                break;
            case 2:
                // Thin, straight and transparent
                mShimmerViewContainer.setBaseAlpha(0.1f);
                mShimmerViewContainer.setDropoff(0.1f);
                mShimmerViewContainer.setTilt(0);
                mTvTitle.setText("Thin, straight and transparent");
                break;
            case 3:
                // Sweep angle 90
                mShimmerViewContainer.setAngle(ShimmerFrameLayout.MaskAngle.CW_90);
                mTvTitle.setText("Sweep angle 90");
                break;
            case 4:
                // Spotlight
                mShimmerViewContainer.setBaseAlpha(0);
                mShimmerViewContainer.setDuration(2000);
                mShimmerViewContainer.setDropoff(0.1f);
                mShimmerViewContainer.setIntensity(0.35f);
                mShimmerViewContainer.setMaskShape(ShimmerFrameLayout.MaskShape.RADIAL);
                mTvTitle.setText("Spotlight");
                break;
        }

        // Setting a value on the shimmer layout stops the animation. Restart it, if necessary.
        if (isPlaying) {
            mShimmerViewContainer.startShimmerAnimation();
        }
    }
}
