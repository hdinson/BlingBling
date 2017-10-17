package dinson.customview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import dinson.customview.R;
import dinson.customview.listener.CalculatorKey;
import dinson.customview.listener.OnItemSwipeOpen;
import dinson.customview.listener._003OnCalculatorInput;
import dinson.customview.listener._003OnRvItemChangeListener;
import dinson.customview.model._003CurrencyModel;
import dinson.customview.utils.GlideUtils;
import dinson.customview.utils.UIUtils;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;
import dinson.customview.weight.swipelayout.SwipeItemLayout;

/**
 * @author Dinson - 2017/7/21
 */
public class _003CurrencyAdapter extends CommonAdapter<_003CurrencyModel> implements _003OnCalculatorInput,
    _003OnRvItemChangeListener {

    private OnItemSwipeOpen mListener;
    private double mTargetMoney;
    private static final float sDefaultMoney = 100;
    private int mCurrentSelect = 1;

    private final Animation mCursorAnimation;

    public _003CurrencyAdapter(Context context, List<_003CurrencyModel> dataList, OnItemSwipeOpen listener) {
        super(context, dataList);
        mListener = listener;

        mCursorAnimation = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim._003_cursor_alpha);
        mCursorAnimation.setRepeatCount(Animation.INFINITE);
        mCursorAnimation.setRepeatMode(Animation.REVERSE);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_003_currency;
    }

    @Override
    public void convert(CommonViewHolder holder, _003CurrencyModel dataBean, int position) {
        GlideUtils.setImage(mContext, dataBean.getImgUrl(), holder.getView(R.id.ivImg));
        holder.setTvText(R.id.tvCurrencyCode, dataBean.getCurrencyCode());
        holder.setTvText(R.id.tvCurrencyCn, String.format(Locale.CHINA, "%s %s", dataBean.getCurrencyCn(), dataBean.getSign()));
        holder.getView(R.id.contentLayout).setEnabled(position == mCurrentSelect);

        //计算汇率结果
        TextView tvResult = holder.getView(R.id.tvResult);
        if (mTargetMoney == 0) {
            tvResult.setText("");
            tvResult.setHint(dataBean.getTargetMoney(sDefaultMoney));
        } else {
            if (mCurrentSelect == position) {
                tvResult.setHint("");
                tvResult.setText(new BigDecimal(mTargetMoney).toString());
            } else {
                tvResult.setHint("");
                tvResult.setText(dataBean.getTargetMoney(mTargetMoney));
            }
        }

        //侧滑布局的监听
        SwipeItemLayout delete = holder.getView(R.id.deleteLayout);
        delete.addSwipeListener((view, isOpen) -> {
            if (isOpen) mListener.onOpen(view, position);
        });

        //处理光标
        View cursor = holder.getView(R.id.lFocusView);
        if (position == mCurrentSelect) {
            cursor.setVisibility(View.VISIBLE);
            cursor.startAnimation(mCursorAnimation);
        } else {
            cursor.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onInput(@NonNull CalculatorKey key) {
        /*String[] split = String.valueOf(mTargetMoney).split("\\.");

        LogUtils.e("1:"+split[0]+" "+split[1]);
        if (split.length == 2 && split[1].length() == 2) {
            //小数点超过2位，晃动提示
            mCurrentTv.startAnimation(mShakeAnimation);
            return;
        }
        if (split.length == 1 && split[0].length() > 11) {
            //整数位超过12位，晃动提示
            mCurrentTv.startAnimation(mShakeAnimation);
            return;
        }*/

        double temp = mTargetMoney;
        switch (key) {
            case N0:
                mTargetMoney = mTargetMoney == 0 ? 0 : mTargetMoney * 10;
                break;
            case N1:
                mTargetMoney = mTargetMoney * 10 + 1;
                break;
            case N2:
                mTargetMoney = mTargetMoney * 10 + 2;
                break;
            case N3:
                mTargetMoney = mTargetMoney * 10 + 3;
                break;
            case N4:
                mTargetMoney = mTargetMoney * 10 + 4;
                break;
            case N5:
                mTargetMoney = mTargetMoney * 10 + 5;
                break;
            case N6:
                mTargetMoney = mTargetMoney * 10 + 6;
                break;
            case N7:
                mTargetMoney = mTargetMoney * 10 + 7;
                break;
            case N8:
                mTargetMoney = mTargetMoney * 10 + 8;
                break;
            case N9:
                mTargetMoney = mTargetMoney * 10 + 9;
                break;
            case ADD:

                break;
            case SUB:

                break;
            case MUL:

                break;
            case DIV:

                break;
            case DOT:

                break;
            case DELETE:
                mTargetMoney = (long) mTargetMoney / 10;
                break;
            case CLEAR:
                mTargetMoney = 0;
                break;
        }
        if (temp != mTargetMoney) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void OnItemChange(int position) {
        mCurrentSelect = position;
        float baseRate = mDataList.get(position).getBaseRate();
        for (_003CurrencyModel currencyModel : mDataList) {
            currencyModel.setTargetRate(baseRate);
        }
        notifyDataSetChanged();
    }
}
