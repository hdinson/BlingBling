package dinson.customview.weight.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.NotNull;

/**
 * RecyclerView的点击监听
 *
 * @author Dinson-2017/7/4
 */
public class OnItemClickListener implements RecyclerView.OnItemTouchListener {


    public interface OnClickListener {
        void onItemClick(@NotNull View view, int position);
    }

    public interface onLongClickListener {
        void onItemLongClick(View view, int position);
    }


    private OnClickListener mListener;
    private onLongClickListener mLongListener;
    private GestureDetector mGestureDetector;

    public OnItemClickListener(Context context, RecyclerView recyclerView, OnClickListener listener) {
        init(context, recyclerView, listener, null);
    }

    public OnItemClickListener(Context context, RecyclerView recyclerView, onLongClickListener listener) {
        init(context, recyclerView, null, listener);
    }

    public OnItemClickListener(Context context, final RecyclerView recyclerView, OnClickListener listener, onLongClickListener longListener) {
        init(context, recyclerView, listener, longListener);
    }

    private void init(Context context, final RecyclerView recyclerView, OnClickListener listener, onLongClickListener longListener) {
        mListener = listener;
        mLongListener = longListener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (childView != null && mLongListener != null) {
                    mLongListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


}
