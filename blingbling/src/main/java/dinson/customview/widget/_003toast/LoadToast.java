package dinson.customview.widget._003toast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Wannes2 on 23/04/2015.
 */
public class LoadToast {
    private String mText = "";
    private LoadToastView mView;
    private ViewGroup mParentView;
    private int mTranslationY = 0;
    private boolean mShowCalled = false;
    private boolean mToastCanceled = false;
    private boolean mInflated = false;
    private boolean mVisible = false;
    private boolean mReAttached = false;

    public LoadToast(Context context) {
        mView = new LoadToastView(context);
        mView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        mParentView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
    }

    private void cleanup() {
        int childCount = mParentView.getChildCount();
        for (int i = childCount; i >= 0; i--) {
            if (mParentView.getChildAt(i) instanceof LoadToastView) {
                LoadToastView ltv = (LoadToastView) mParentView.getChildAt(i);
                ltv.cleanup();
                mParentView.removeViewAt(i);
            }
        }

        mInflated = false;
        mToastCanceled = false;
    }

    public LoadToast setTranslationY(int pixels) {
        mTranslationY = pixels;
        return this;
    }

    public LoadToast setText(String message) {
        mText = message;
        mView.setText(mText);
        return this;
    }

    public LoadToast setTextColor(int color) {
        mView.setTextColor(color);
        return this;
    }

    public LoadToast setBackgroundColor(int color) {
        mView.setBackgroundColor(color);
        return this;
    }

    public LoadToast setProgressColor(int color) {
        mView.setProgressColor(color);
        return this;
    }

    public LoadToast setTextDirection(boolean isLeftToRight) {
        mView.setTextDirection(isLeftToRight);
        return this;
    }

    public LoadToast setBorderColor(int color) {
        mView.setBorderColor(color);
        return this;
    }

    public LoadToast setBorderWidthPx(int width) {
        mView.setBorderWidthPx(width);
        return this;
    }

    public LoadToast setBorderWidthRes(int resourceId) {
        mView.setBorderWidthRes(resourceId);
        return this;
    }

    public LoadToast setBorderWidthDp(int width) {
        mView.setBorderWidthDp(width);
        return this;
    }

    public LoadToast show() {
        mShowCalled = true;
        attach();
        return this;
    }

    private void showInternal() {
        mView.show();
        mView.setTranslationX( (mParentView.getWidth() - mView.getWidth()) / 2);
        mView.setAlpha(  0f);
        mView.setTranslationY( -mView.getHeight() + mTranslationY);
        //mView.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mView, "alpha", 1f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mView, "translationY", 25 + mTranslationY);
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.play(alpha).with(translationY);
        set.start();


        mVisible = true;
    }

    private void attach() {
        cleanup();

        mReAttached = true;

        mParentView.addView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mView.setAlpha(0f);
        mParentView.postDelayed(() -> {
            mView.setTranslationX((mParentView.getWidth() - mView.getWidth()) / 2);
            mView.setTranslationY(-mView.getHeight() + mTranslationY);
            mInflated = true;
            if (!mToastCanceled && mShowCalled) showInternal();
        }, 1);
    }

    public void success() {
        if (!mInflated) {
            mToastCanceled = true;
            return;
        }
        if (mReAttached) {
            mView.success();
            slideUp();
        }
    }

    public void error() {
        if (!mInflated) {
            mToastCanceled = true;
            return;
        }
        if (mReAttached) {
            mView.error();
            slideUp();
        }
    }

    public void hide() {
        if (!mInflated) {
            mToastCanceled = true;
            return;
        }
        if (mReAttached) {
            slideUp(0);
        }
    }

    private void slideUp() {
        slideUp(1000);
    }

    private void slideUp(int startDelay) {
        mReAttached = false;

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mView, "alpha", 0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mView, "translationY", -mView.getHeight() + mTranslationY);
        set.setDuration(300);
        set.setInterpolator(new AccelerateInterpolator());
        set.play(alpha).with(translationY);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mReAttached) {
                    cleanup();
                }
            }
        });
        set.setStartDelay(startDelay);
        set.start();

        mVisible = false;
    }
}
