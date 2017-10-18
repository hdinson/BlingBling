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
import java.util.regex.Pattern;

import dinson.customview.R;
import dinson.customview.listener.CalculatorKey;
import dinson.customview.listener.OnItemSwipeOpen;
import dinson.customview.listener._003OnCalculatorInput;
import dinson.customview.listener._003OnRvItemChangeListener;
import dinson.customview.model._003CurrencyModel;
import dinson.customview.utils.ArithmeticUtils;
import dinson.customview.utils.GlideUtils;
import dinson.customview.utils.LogUtils;
import dinson.customview.utils.StringUtils;
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
    private double mTargetMoney = Integer.MAX_VALUE;
    private String mEquationStr = "";
    private static final float sDefaultMoney = 100;
    public int mCurrentSelect = 0;

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
        holder.setTvText(R.id.tvEquation, dataBean.getEquation()
            .replaceAll("\\+", "﹢").replaceAll("-", "﹣").replaceAll("\\*", "×").replaceAll("/", "÷"));
        holder.setTvText(R.id.tvCurrencyCn, String.format(Locale.CHINA, "%s %s", dataBean.getCurrencyCn(), dataBean.getSign()));
        holder.getView(R.id.contentLayout).setEnabled(position == mCurrentSelect);


        //计算汇率结果
        TextView tvResult = holder.getView(R.id.tvResult);
        if (mTargetMoney == Integer.MAX_VALUE) {
            tvResult.setText("");
            tvResult.setHint(dataBean.getTargetMoney(sDefaultMoney));
        } else {
            if (mCurrentSelect == position && StringUtils.isEmpty(dataBean.getEquation())) {
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
        View lCursor = holder.getView(R.id.lFocusView);
        View sCursor = holder.getView(R.id.sFocusView);
        if (position != mCurrentSelect) {
            lCursor.setVisibility(View.GONE);
            sCursor.setVisibility(View.GONE);
            return;
        }

        if (StringUtils.isEmpty(dataBean.getEquation())) {
            sCursor.setVisibility(View.GONE);
            lCursor.setVisibility(View.VISIBLE);
            lCursor.startAnimation(mCursorAnimation);
        } else {
            lCursor.setVisibility(View.GONE);
            sCursor.setVisibility(View.VISIBLE);
            sCursor.startAnimation(mCursorAnimation);

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

        if (!validateInput(key)) {
            //验证不过,晃动提示
            UIUtils.showToast("验证不过,晃动提示");
            return;
        }


        double temp = mTargetMoney;
        switch (key) {
            case N0:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "0");
                break;
            case N1:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "1");
                break;
            case N2:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "2");
                break;
            case N3:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "3");
                break;
            case N4:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "4");
                break;
            case N5:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "5");
                break;
            case N6:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "6");
                break;
            case N7:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "7");
                break;
            case N8:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "8");
                break;
            case N9:
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr += "9");
                break;
            case ADD:
                mEquationStr = isEndWithNum(mEquationStr) ? mEquationStr + "+"
                    : mEquationStr.substring(0, mEquationStr.length() - 1) + "+";
                break;
            case SUB:
                mEquationStr = isEndWithNum(mEquationStr) ? mEquationStr + "-"
                    : mEquationStr.substring(0, mEquationStr.length() - 1) + "-";
                break;
            case MUL:
                mEquationStr = isEndWithNum(mEquationStr) ? mEquationStr + "*"
                    : mEquationStr.substring(0, mEquationStr.length() - 1) + "*";
                break;
            case DIV:
                mEquationStr = isEndWithNum(mEquationStr) ? mEquationStr + "/"
                    : mEquationStr.substring(0, mEquationStr.length() - 1) + "/";
                break;
            case DOT:
                mEquationStr += ".";
                break;
            case DELETE:
                if (mEquationStr.length() == 0) return;
                String equation = mEquationStr.substring(0, mEquationStr.length() - 1);
                char end = equation.charAt(equation.length() - 1);
                if (end != '-' || end != '+' || end != '*' || end != '/') {
                    LogUtils.e("不相等");
                } else {
                    LogUtils.e("相等");
                }
                //mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr + "9");
                break;
            case CLEAR:
                mTargetMoney = 0;
                break;
        }
        if (containsOperator(mEquationStr)) {
            mDataList.get(mCurrentSelect).setEquation(mEquationStr);
            notifyItemChanged(mCurrentSelect);
        }
        if (temp != mTargetMoney) {
            notifyDataSetChanged();
        }
    }

    private boolean validateInput(CalculatorKey key) {
        return true;
    }

    /**
     * 是否包含运算符
     */
    private boolean containsOperator(String str) {
        return str.contains("+") || str.contains("-") || str.contains("*") || str.contains("/");
    }

    /**
     * 是否以数字结尾
     */
    private boolean isEndWithNum(String str) {
        return Pattern.matches("(.*\\d+$)", str);
    }


    @Override
    public void OnItemChange(int position) {
        //删除旧算式表达式赋给选中项
        _003CurrencyModel oldBean = mDataList.get(mCurrentSelect);
        _003CurrencyModel newBean = mDataList.get(position);
        newBean.setEquation(oldBean.getEquation());
        oldBean.setEquation("");

        mCurrentSelect = position;
        double baseRate = newBean.getBaseRate();
        for (_003CurrencyModel currencyModel : mDataList) {
            currencyModel.setTargetRate(baseRate);
        }
        notifyDataSetChanged();
    }
}
