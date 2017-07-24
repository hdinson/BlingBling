package dinson.customview.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import dinson.customview.entity.one.DailyDetail;
import dinson.customview.entity.one.DailyList;


public class CacheUtils {

    public static void setMainHeardCache(DailyList bean) {
        String json = new Gson().toJson(bean);
        LogUtils.e("put to cache>>>"+json);
        setCache("home_heardlist", json, 3600000);
    }

    public static DailyList getMainHeardCache() {
        String homelist = getCache("home_heardlist");
        LogUtils.e("get from cache>>"+homelist);
        if (homelist == null) return null;
        return new Gson().fromJson(homelist, DailyList.class);
    }

    public static void setDailyDetail(DailyDetail bean) {
        String json = new Gson().toJson(bean);
        setCache("home_heard_detail" + bean.getData().getHpcontent_id(), json);
    }

    public static DailyDetail getDailyDetail(int id) {
        String cache = getCache("home_heard_detail" + id);
        if (cache == null) return null;
        return new Gson().fromJson(cache, DailyDetail.class);
    }

    //////////////////////////////////分割线//////////////////////////////////////////////////////

    private static void setCache(String url, String json) {
        setCache(url, json, 0);
    }

    private static void setCache(String url, String json, long deathLine) {
        File cacheDir = UIUtils.getContext().getCacheDir();
        File cacheFile = new File(cacheDir, MD5.encode(url));
        FileWriter fw = null;
        try {
            fw = new FileWriter(cacheFile);
            if (deathLine != 0) {
                deathLine = System.currentTimeMillis() + deathLine;
            }
            //0表示没有有效期
            fw.write(deathLine + "\n");
            fw.write(json);
            fw.flush();
        } catch (IOException e) {
            LogUtils.e(e);
        } finally {
            IOUtils.close(fw);
        }
    }

    private static String getCache(String url) {
        File cacheDir = UIUtils.getContext().getCacheDir();
        File cacheFile = new File(cacheDir, MD5.encode(url));

        if (cacheFile.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                long deathLine = Long.parseLong(reader.readLine());

                if (System.currentTimeMillis() < deathLine || deathLine == 0) {

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();

                }
            } catch (Exception e) {
                return null;
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }
}
