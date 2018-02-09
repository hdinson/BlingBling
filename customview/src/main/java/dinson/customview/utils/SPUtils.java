package dinson.customview.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;

/**
 * SharePreference封装
 */
public class SPUtils {

    public static void setUserCurrency(String... currency) {
        if (currency.length != 5) return;
        String value = "";
        for (int i = 0; i < currency.length; i++) {
            if (i != currency.length - 1)
                value += currency[i].toUpperCase() + ",";
            else value += currency[i].toUpperCase();
        }
        putString(UIUtils.getContext(), "config", "currency", value);
    }

    public static List<String> getUserCurrency() {
        String value = getString(UIUtils.getContext(), "config", "currency", null);
        if (value != null) {
            List<String> result = Arrays.asList(value.split(","));
            Observable.fromIterable(result)
                .filter(s -> !StringUtils.isEmpty(s))
                .collect(ArrayList::new, (BiConsumer<ArrayList<String>, String>) ArrayList::add)
                .subscribe();
            return result;
        }
        return null;
    }


    /**
     * 判断是否应该加载默认的应用图标
     */
    public static boolean isDefaultAppIcon(Context context) {
        boolean aBoolean = getBoolean(context, "config", "icon", false);
        putBoolean(context, "config", "icon", !aBoolean);
        return aBoolean;
    }


    /////////////////////////////////// 分割线 /////////////////////////////////////////////////////

    public static boolean getBoolean(Context ctx, String fileName, String key, boolean defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putBoolean(Context ctx, String fileName, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void putString(Context ctx, String fileName, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context ctx, String fileName, String key, String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(Context ctx, String fileName, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(Context ctx, String fileName, String key, int defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }
}
