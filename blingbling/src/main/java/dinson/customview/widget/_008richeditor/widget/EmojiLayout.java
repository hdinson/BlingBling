package dinson.customview.widget._008richeditor.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import dinson.customview.R;
import dinson.customview.widget._008richeditor.adapter.ExpressionPagerAdapter;
import dinson.customview.widget._008richeditor.adapter.SmileImageExpressionAdapter;

import static dinson.customview.kotlin.LogExtentionKt.logi;


/**
 * Created by MryU93 on 2017/6/15.
 * Desc:
 */

public class EmojiLayout extends LinearLayout {

    private static int[] IMG_LIST = new int[]{

    };


    private RichEditor editTextEmoji;
    private List<String> reslist = new ArrayList<>();
    private ImageView[] imageFaceViews;
    private LinearLayout mContainer;
    private ViewPager mVPager;

    public EmojiLayout(Context context) {
        super(context);
        init(context);
    }

    public EmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmojiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    private void init(Context context) {

        setOrientation(VERTICAL);
        mVPager = new ViewPager(context);
        LayoutParams vpParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        vpParams.weight = 4;
        addView(mVPager, vpParams);

        mContainer = new LinearLayout(context);
        LayoutParams llParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        llParams.weight = 1;
        mContainer.setGravity(Gravity.CENTER);
        mContainer.setOrientation(HORIZONTAL);
        addView(mContainer, llParams);

        if (isInEditMode())
            return;
        initViews();
    }


    /**
     * 初始化View
     */
    private void initViews() {

        logi(() -> "emoji 的高度：" + getMeasuredHeight());

        // ViewPager mVPager = (ViewPager) findViewById(R.id.edittext_bar_vPager);
        //LinearLayout edittextBarViewGroupFace = (LinearLayout) findViewById(R.id.edittext_bar_viewGroup_face);


        int size = dip2px(getContext(), 5);
        int marginSize = dip2px(getContext(), 5);
        // 表情list
        List<String> smile = new ArrayList<>();
        for (int i = 1; i <= IMG_LIST.length; i++) {
            smile.add("[e" + i + "]");
        }
        reslist.addAll(smile);
        // 初始化表情viewpager
        List<View> views = new ArrayList<>();
        int gridChildSize = (int) Math.ceil((double) reslist.size() / (double) 21);
        for (int i = 1; i <= gridChildSize; i++) {
            views.add(getGridChildView(i));
        }
        ImageView imageViewFace;
        imageFaceViews = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            LayoutParams margin = new LayoutParams(size, size);
            margin.setMargins(marginSize, 0, 0, 0);
            imageViewFace = new ImageView(getContext());
            imageViewFace.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            imageFaceViews[i] = imageViewFace;
            if (i == 0) {
                imageFaceViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageFaceViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            mContainer.addView(imageFaceViews[i], margin);
        }
        mVPager.setAdapter(new ExpressionPagerAdapter(views));
        mVPager.addOnPageChangeListener(new GuidePageChangeListener());
    }

    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    /**
     * 获取表情的gridview的子view
     */
    private View getGridChildView(int i) {
        //View view = View.inflate(getContext(), R.layout.expression_gridview, null);
        //LockGridView gv = (LockGridView) view.findViewById(R.id.gridview);

        LockGridView gv = new LockGridView(getContext());
        gv.setNumColumns(7);
        gv.setCacheColorHint(Color.parseColor("#eeeeee"));
        gv.setGravity(Gravity.CENTER);
        gv.setVerticalSpacing(dip2px(getContext(), 20));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        gv.setLayoutParams(params);

        List<String> list = new ArrayList<>();
        int startInd = (i - 1) * 21;
        if ((startInd + 21) >= reslist.size()) {
            list.addAll(reslist.subList(startInd, startInd + (reslist.size() - startInd)));
        } else {
            list.addAll(reslist.subList(startInd, startInd + 21));
        }
        final SmileImageExpressionAdapter smileImageExpressionAdapter = new SmileImageExpressionAdapter(getContext(), 1, list);
        gv.setAdapter(smileImageExpressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = smileImageExpressionAdapter.getItem(position);
                editTextEmoji.insertIcon(filename);
            }
        });

        return gv;
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageFaceViews.length; i++) {
                imageFaceViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);

                if (arg0 != i) {
                    imageFaceViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        Activity context = (Activity) getContext();
        if (context.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (context.getCurrentFocus() != null) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        editTextEmoji.requestFocus();
        InputMethodManager inputManager =
            (InputMethodManager) editTextEmoji.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editTextEmoji, 0);
    }


    public EditText getEditTextSmile() {
        return editTextEmoji;
    }

    public void setEditTextSmile(RichEditor editTextSmile) {
        this.editTextEmoji = editTextSmile;
        editTextSmile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });
    }
}