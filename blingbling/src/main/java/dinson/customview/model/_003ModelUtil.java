package dinson.customview.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dinson.customview.utils.IOUtils;
import dinson.customview.utils.StringUtils;
import dinson.customview.utils.UIUtils;

/**
 * google VR 数据模型
 */
public class _003ModelUtil {

    public static ArrayList<_003CurrencyModel> getCurrencyList() {
        return getFileFromAssets("currency.txt");
    }

    private static ArrayList<_003CurrencyModel> getFileFromAssets(String fileName) {
        Context context = UIUtils.getContext();
        if (context == null || StringUtils.isEmpty(fileName)) return null;

        BufferedReader br = null;
        InputStreamReader in = null;
        try {
            ArrayList<_003CurrencyModel> result = new ArrayList<>();

            in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            br = new BufferedReader(in);

            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                result.add(new _003CurrencyModel(split[0], split[1], split[2], split[3]));
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.INSTANCE.close(br);
            IOUtils.INSTANCE.close(in);
        }
    }
}


