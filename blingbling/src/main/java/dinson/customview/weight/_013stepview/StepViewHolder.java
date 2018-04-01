package dinson.customview.weight._013stepview;

import android.content.Context;
import android.view.View;

import org.jetbrains.annotations.NotNull;


public interface StepViewHolder<T> {
    /**
     * 创建View并绑定数据
     */
    View onBind(@NotNull Context context, @NotNull T data, @NotNull Position pos, @NotNull State state);
}
