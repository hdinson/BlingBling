package dinson.customview.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    /**
     * 获取当前13位时间戳
     */
    public static long getCurrentTimeMillis13() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前10位时间戳
     */
    public static int getCurrentTimeMillis10() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 将时间戳转为字符串
     *
     * @param time 默认格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String long2Str(long time) {
        return long2Str(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将时间戳转为字符串
     *
     * @param time   要转化的时间
     * @param format 时间格式
     * @return
     */
    public static String long2Str(long time, String format) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        re_StrTime = sdf.format(new Date(time));
        return re_StrTime;
    }

    /**
     * 将时间戳转为字符串
     *
     * @param time   要转化的时间
     * @param format 时间格式
     * @return
     */
    public static String int2Str(int time, String format) {
        return long2Str(time * 1000L, format);
    }

    /**
     * 将时间戳转为字符串
     *
     * @param time 默认格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String int2Str(int time) {
        return int2Str(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 字符串时间转化为时间戳
     *
     * @param time   字符串时间
     * @param format 时间格式
     * @return 时间戳 返回0表示时间格式错误
     */
    public static int str2int(String time, String format) {
        int re_time = 0;
        try {
            re_time = (int) (new SimpleDateFormat(format).parse(time).getTime() / 1000);
        } catch (ParseException e) {
            return re_time;
        }
        return re_time;
    }

    /**
     * 得到日期
     *
     * @param offset 偏移天数，0表示今天，-1表示昨天，1表示明天
     * @return yyyy-MM-_dd
     */
    public static String getDate(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, offset);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 得到今年
     *
     * @return yyyy
     */
    public static String getYear() {
        return getYear(0);
    }

    /**
     * 得到日期
     *
     * @param offset 偏移天数，0表示今年，-1表示去年，1表示明年
     * @return yyyy
     */
    public static String getYear(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, offset);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(calendar.getTime());
        return year;
    }

    /**
     * 得到日期时间戳
     *
     * @param offset 偏移天数，0表示今天，-1表示昨天，1表示明天
     * @return 10位时间戳
     */
    public static int getDataTimestamp(int offset) {
        return str2int(getDate(offset), "yyyy-MM-dd");
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，分钟前，小时前，
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如5分钟前，1小时前，2小时前，3小时前...昨天，2天前，3天前，4天前...
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(int timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time >= 0 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return "昨天";
        } else if (time >= 3600 * 48) {
            return time / 3600 / 24 + "天前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如12:14，昨天 12:14，前天 12:14，
     *
     * @param timestamp
     * @return
     */
    public static String convertTimestamp(int timestamp) {
        int today = getDataTimestamp(0);
        int yesT = getDataTimestamp(-1);
        int beYesT = getDataTimestamp(-2);
        if (timestamp >= today) {
            return int2Str(timestamp, "HH:mm");
        } else if (timestamp < today && timestamp >= yesT) {
            return "昨天  " + int2Str(timestamp, "HH:mm");
        } else if (timestamp < yesT && timestamp >= beYesT) {
            return "前天  " + int2Str(timestamp, "HH:mm");
        } else {
            return int2Str(timestamp, "MM月dd日  HH:mm");
        }
    }

    /**
     * 获取最大时间
     *
     * @param time1
     * @param time2
     * @param format 按格式比较
     * @return
     * @throws ParseException 时间转化异常
     */
    public static String getMax(String time1, String time2, String format) throws Exception {
        int compare = compareTime(time1, time2, format);
        String result = compare >= 0 ? time1 : time2;
        return result;
    }

    ;

    /**
     * 比较两个时间大小
     *
     * @param time1
     * @param time2
     * @param format 按格式比较
     * @return 正数：第一个大
     * 负数：第二个大
     * 0：一样大
     * 绝对值：相差秒数
     * @throws ParseException 时间转化异常
     */
    public static int compareTime(String time1, String time2, String format) throws Exception {
        SimpleDateFormat dfs = new SimpleDateFormat(format);
        Date begin = null;
        Date end = null;
        begin = dfs.parse(time1);
        end = dfs.parse(time2);
        return (int) (begin.getTime() - end.getTime()) / 1000;
    }

    /**
     * 将一个时间转化成另一种格式
     *
     * @param time
     * @param oldFormat 转化前的格式
     * @param newFormat 转化后的格式
     * @return 返回null表示时间格式错误
     */
    public static String convertStr(String time, String oldFormat, String newFormat) {
        SimpleDateFormat oldSdf = new SimpleDateFormat(oldFormat);
        SimpleDateFormat newSdf = new SimpleDateFormat(newFormat);
        String format = null;
        try {
            format = newSdf.format(oldSdf.parse(time));
        } catch (ParseException e) {
            return format;
        }
        return format;
    }

    /**
     * 获取当前时间
     *
     * @param type 格式
     * @return
     */
    public static String getCurrentDateTime(String type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(type);
        return dateFormat.format(new Date());
    }

	/**
	 * 将一个秒数转换为 xx:xx:xx的时间样式。如传入5 则范围 00:00:05
	 * @param seconds 秒
	 * @return x天 xx:xx:xx的时间样式
	 */
	public static String calcTimeInFormat(int seconds){
		StringBuilder builder = new StringBuilder();
		final int _hour = 3600;//小时换算基数
		final int _minute = 60;//分钟换算基数
		if (seconds > 0) {
			long hour = seconds / _hour;
			if (hour > 0) {
				if (hour < 10) builder.append(0);
				builder.append(hour).append(':');
			}else builder.append("00:");
			long minute = (seconds % _hour) / _minute;
			if (minute < 10) builder.append(0);
			builder.append(minute).append(':');
			long second = seconds % _minute;
			if (second < 10) builder.append(0);
			builder.append(second).append(':');
		} else return "00:00:00";
		return builder.toString();
    }

	/**
	 * 转换服务器时间。默认服务器时间为 "yyyy-MM-dd HH:mm:ss" 样式，失败返回当前时间
	 * @param datetimeStr
	 * @return
	 */
	public static Date convertServerDateTime(String datetimeStr){
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = null;
	    try {
		    date = dateFormat.parse(datetimeStr);
	    } catch (ParseException e) {
		    date = new Date();
		    e.printStackTrace();
	    }
	    return date;
    }

	/**
	 * 转换成默认服务器时间样式。默认服务器时间为 "yyyy-MM-dd HH:mm:ss" 样式，失败返回当前时间
	 * @param date
	 * @return
	 */
	public static String convertToServerFormat(Date date){
		if (date == null) date = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return dateFormat.format(date);
    }
}
