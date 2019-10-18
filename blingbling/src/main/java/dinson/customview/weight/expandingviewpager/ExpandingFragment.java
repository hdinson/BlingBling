package dinson.customview.weight.expandingviewpager;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import dinson.customview.R;


public abstract class ExpandingFragment extends Fragment {

    private static final float SCALE_OPENED = 1.2f;
    private static final int SCALE_CLOSED = 1;
    private static final int ELEVATION_OPENED = 40;

    Fragment fragmentFront;
    Fragment fragmentBottom;

    private FrameLayout back;
    private CardView front;
    private FrameLayout layout3;

    private float startY;

    float defaultCardElevation;
    private OnExpandingClickListener mListener;
    private ObjectAnimator frontAnimator;
    private ObjectAnimator backAnimator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expanding_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fragmentFront = getFragmentTop();
        this.fragmentBottom = getFragmentBottom();

        if (fragmentFront != null && fragmentBottom != null) {
            getChildFragmentManager().beginTransaction()
                .replace(R.id.front, fragmentFront)
                .replace(R.id.bottomLayout, fragmentBottom)
                .commit();
        }

        back =   view.findViewById(R.id.back);
        front =   view.findViewById(R.id.front);
        layout3 =  view.findViewById(R.id.bottomLayout);
        view.setOnClickListener(new OnClick());
        setupDownGesture(view);

        defaultCardElevation = front.getCardElevation();
    }

    private void setupDownGesture(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isOpened() && event.getY() - startY > 0) {
                            close();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExpandingClickListener) {
            mListener = (OnExpandingClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + "ExpandingFragment must implement OnExpandingClickListener");
        }
    }

    public abstract Fragment getFragmentTop();

    public abstract Fragment getFragmentBottom();

    public boolean isClosed() {
        return back.getScaleX() == SCALE_CLOSED;
    }

    public boolean isOpened() {
        return back.getScaleX() == SCALE_OPENED;
    }

    public void toggle() {
        if (isClosed()) {
            open();
        } else {
            close();
        }
    }

    public void open() {
        ViewGroup.LayoutParams layoutParams = layout3.getLayoutParams();
        layoutParams.height = (int) (front.getHeight() * SCALE_OPENED / 4 * SCALE_OPENED);
        layout3.setLayoutParams(layoutParams);


        back.setPivotY(0);

        PropertyValuesHolder front1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, -front.getHeight() / 4);
        PropertyValuesHolder front2 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 1);
        frontAnimator = ObjectAnimator.ofPropertyValuesHolder(front, front1, front2);
        PropertyValuesHolder backX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f);
        PropertyValuesHolder backY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f);
        backAnimator = ObjectAnimator.ofPropertyValuesHolder(back, backX, backY);
        back.setPivotY(0);
        frontAnimator.start();
        backAnimator.start();

        front.setCardElevation(ELEVATION_OPENED);
    }

    public void close() {
        if (frontAnimator != null) {
            frontAnimator.reverse();
            backAnimator.reverse();
            backAnimator = null;
            frontAnimator = null;
        }
        front.setCardElevation(defaultCardElevation);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (isOpened()) {
                if (mListener != null) {
                    mListener.onExpandingClick(v);
                }
            } else {
                open();
            }
        }
    }

    public interface OnExpandingClickListener {
        void onExpandingClick(View v);
    }
}
