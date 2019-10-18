package dinson.customview.entity.gank;


import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MeiZiTuNineGrid {

    private String time;
    private boolean showYear = false;

    private ArrayList<Uri> pictureList = new ArrayList<>();
    private ArrayList<Uri> pictureThumbList = new ArrayList<>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Uri> getPictureList() {
        return pictureList;
    }

    public void setPictureList(ArrayList<Uri> pictureList) {
        this.pictureList = pictureList;
    }

    public List<Uri> getPictureThumbList() {
        return pictureThumbList;
    }

    public void setPictureThumbList(ArrayList<Uri> pictureThumbList) {
        this.pictureThumbList = pictureThumbList;
    }

    public boolean isShowYear() {
        return showYear;
    }

    public void setShowYear(boolean showYear) {
        this.showYear = showYear;
    }
}
