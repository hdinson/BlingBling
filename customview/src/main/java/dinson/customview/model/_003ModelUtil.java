package dinson.customview.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dinson.customview.utils.IOUtils;
import dinson.customview.utils.SPUtils;
import dinson.customview.utils.StringUtils;
import dinson.customview.utils.UIUtils;

/**
 * google VR 数据模型
 */
public class _003ModelUtil {
    public static List<_003CurrencyModel> getCurrencyList(int limit) {
        return getFileFromAssets("currency.txt", limit);
    }

    /**
     * 获取数据模型
     */
    private static ArrayList<_003CurrencyModel> getFileFromAssets(String fileName, int limit) {

        Context context = UIUtils.getContext();
        if (context == null || StringUtils.isEmpty(fileName)) return null;

        BufferedReader br = null;
        InputStreamReader in = null;
        try {
            ArrayList<_003CurrencyModel> result = new ArrayList<>();
            List<String> userCurrency = SPUtils.getUserCurrency();

            in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            br = new BufferedReader(in);
            if (userCurrency != null) {
                //获取用户指定的货币
                String line;
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(",");
                    if (userCurrency.contains(split[1])) {
                        result.add(new _003CurrencyModel(split[0], split[1], split[2], split[3]));
                    }
                }
                return result;
            } else {
                //获取默认前limit个
                for (int i = 0; i < limit; i++) {
                    String line = br.readLine();
                    String[] split = line.split(",");
                    result.add(new _003CurrencyModel(split[0], split[1], split[2], split[3]));
                }
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.close(br);
            IOUtils.close(in);
        }
    }
}


