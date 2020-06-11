package dinson.customview.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.service.quicksettings.Tile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dinson.customview.utils.LogUtils;

public class _025Schedule implements Parcelable {
    private String id;
    private String name;
    private String dateTime;//yyyy-MM-dd
    private int repeatType;
    private String displayDay;//显示的天数，需要经过计算


    protected _025Schedule(Parcel in) {
        id = in.readString();
        name = in.readString();
        dateTime = in.readString();
        repeatType = in.readInt();
        displayDay = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(dateTime);
        dest.writeInt(repeatType);
        dest.writeString(displayDay);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<_025Schedule> CREATOR = new Creator<_025Schedule>() {
        @Override
        public _025Schedule createFromParcel(Parcel in) {
            return new _025Schedule(in);
        }

        @Override
        public _025Schedule[] newArray(int size) {
            return new _025Schedule[size];
        }
    };

    @Override
    public String toString() {
        return "Schedule{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", dateTime='" + dateTime + '\'' +
            '}';
    }

    public _025Schedule(String id, String name, String dateTime) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
    }

    public _025Schedule(String id, String name, String dateTime, int repeatType) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.repeatType = repeatType;
    }

    /**
     * 计算时间间隔，带正负数
     *
     * @return
     * @throws Exception
     */
    public int getDisplayDay() throws Exception {
        if (displayDay == null || displayDay.length() == 0) {
            switch (repeatType) {
                case 0:
                    //不重复
                    return (int) compareFuture(dateTime);
                case 1:
                    //每周重复
                    return getRepeatDay(Calendar.WEEK_OF_MONTH);
                case 2:
                    //每月重复
                    return getRepeatDay(Calendar.MONTH);
                case 3:
                    //每年重复
                    return getRepeatDay(Calendar.YEAR);
                default:
                    return 0;
            }
        } else return Integer.parseInt(displayDay);
    }

    private long compareFuture(String future) throws ParseException {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date end = dfs.parse(future);
        String now = dfs.format(new Date());
        Date start = dfs.parse(now);
        long time = (end.getTime() - start.getTime()) / 100000 / 36 / 24;
        return time < 0 ? time - 1 : time;
    }

    private int getRepeatDay(int field) throws ParseException {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date target = dfs.parse(dateTime);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(target);
        int i = 1;
        String nowStr = dfs.format(new Date());
        long now = dfs.parse(nowStr).getTime();
        Calendar c3 = (Calendar) c2.clone();
        while (c3.getTime().getTime() < now) {
            c3 = (Calendar) c2.clone();
            c3.add(field, i);
            i++;
        }
        return (int) ((c3.getTime().getTime() - now) / 100000 / 36 / 24);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getWeek() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date parse = format.parse(dateTime);
            SimpleDateFormat formatWeek = new SimpleDateFormat("EEEE", Locale.CHINA);
            return formatWeek.format(parse);
        } catch (ParseException e) {
            return "异常";
        }
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

}
