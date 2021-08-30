package dinson.customview.widget.dialog;

import org.jetbrains.annotations.NotNull;

/**
 *是否登录成功监听
 */
public interface OnLoginSuccessListener {
    void onSuccess(@NotNull Boolean isSuccess,@NotNull String errorMsg);
}