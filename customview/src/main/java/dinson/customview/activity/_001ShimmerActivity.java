package dinson.customview.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.weight.ShimmerFrameLayout;
 
public class _001ShimmerActivity extends BaseActivity {
    private ShimmerFrameLayout mShimmerViewContainer;
    private Button[] mPresetButtons;
    private int mCurrentPreset = -1;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__001_shimmer);

        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mPresetButtons = new Button[]{
            (Button) findViewById(R.id.preset_button0),
            (Button) findViewById(R.id.preset_button1),
            (Button) findViewById(R.id.preset_button2),
            (Button) findViewById(R.id.preset_button3),
            (Button) findViewById(R.id.preset_button4),
        };
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
