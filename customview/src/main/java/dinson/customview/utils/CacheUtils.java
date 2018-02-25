package dinson.customview.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import dinson.customview._global.GlobalApplication;
import dinson.customview.entity.ExchangeBean;
import dinson.customview.entity.HomeWeather;
import dinson.customview.entity.one.DailyDetail;
import dinson.customview.entity.one.DailyList;


public class CacheUtils {

    /**
     * 设置首页头部one缓存
     *
     * @param bean entity
     */
    public static void setMainHeardCache(DailyList bean) {
        String json = new Gson().toJson(bean);
        LogUtils.d("<DailyList> Put Cache >> " + json, false);
        //缓存的时间是到凌晨4点
        long deathLine = ((long) DateUtils.getDataTimestamp(1) + 14400) * 1000 - System.currentTimeMillis();
        setCache("home_head_list", json, deathLine);
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    public static DailyList getMainHeardCache() {
        String homeList = getCache("home_head_list");
        LogUtils.d("<DailyList> Get Cache << " + homeList, false);
        if (homeList == null) return null;
        return new Gson().fromJson(homeList, DailyList.class);
    }

    /**
     * 设置首页头部one详情缓存
     *
     * @param bean entity
     */
    public static void setDailyDetail(DailyDetail bean) {
        String json = new Gson().toJson(bean);
        setCache("home_heard_detail" + bean.getData().getHpcontent_id(), json);
    }

    /**
     * 根据id获取首页头部one详情缓存
     *
     * @param id 数据id
     * @return null表示没有数据
     */
    public static DailyDetail getDailyDetail(int id) {
        String cache = getCache("home_heard_detail" + id);
        if (cache == null) return null;
        return new Gson().fromJson(cache, DailyDetail.class);
    }


    /**
     * 设置首页头部one缓存
     *
     * @param bean entity
     */
    public static void setHomeWeatherCache(HomeWeather bean) {
        String json = new Gson().toJson(bean);
        LogUtils.d("<HomeWeather> Put Cache >> " + json, false);
        setCache("lastKnowWeather", json, 3600000);//缓存时间1小时
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    public static HomeWeather getHomeWeatherCache(String city) {
        String homeList = getCache("lastKnowWeather");
        if (homeList == null) {
            LogUtils.d("<HomeWeather> is out of date or no exist !", false);
            return null;
        }
        HomeWeather homeWeather = new Gson().fromJson(homeList, HomeWeather.class);
        String cacheName = homeWeather.getResults().get(0).getLocation().getName();
        if (cacheName.contains(city) || city.contains(cacheName)) {
            LogUtils.d("<HomeWeather> Get Cache << " + homeList, false);
            return homeWeather;
        }
        LogUtils.d("<HomeWeather> LocationCity is change!", false);
        return null;
    }

    /**
     * 设置汇率兑换比率缓存
     *
     * @param beanStr entity序列化后
     */
    public static void setExangeRateCache(String beanStr) {
        if (GlobalApplication.IS_DEBUG) {
            String json = new Gson().toJson(beanStr);
            LogUtils.d("<ExchangeBean> Put Cache >> " + json, false);
        }
        setCache("lastKnowExchangeRate", beanStr);//缓存永久有效
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    public static String getExchangeRateCache() {
        String exchangeRate = getCache("lastKnowExchangeRate");
        if (exchangeRate == null) {
            LogUtils.d("<ExchangeBean> is out of date or no exist !", false);
            return null;
        }
        if (GlobalApplication.IS_DEBUG) {
            ExchangeBean exchangeBean = new Gson().fromJson(exchangeRate, ExchangeBean.class);
            LogUtils.d("<ExchangeBean> Get Cache << " + exchangeBean, false);
        }
        return exchangeRate;
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
            e.printStackTrace();
        } finally {
            IOUtils.INSTANCE.close(fw);
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
                IOUtils.INSTANCE.close(reader);
            }
        }
        return null;
    }
}
