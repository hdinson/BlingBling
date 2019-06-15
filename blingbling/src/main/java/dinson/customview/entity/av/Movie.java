package dinson.customview.entity.av;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String code;
    private String date;
    private String coverUrl;
    private String link;
    private boolean hot;

    public Movie(String title, String code, String date, String coverUrl, String link, boolean hot) {
        this.title = title;
        this.code = code;
        this.date = date;
        this.coverUrl = coverUrl;
        this.link = link;
        this.hot = hot;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        code = in.readString();
        date = in.readString();
        coverUrl = in.readString();
        link = in.readString();
        hot = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(code);
        dest.writeString(date);
        dest.writeString(coverUrl);
        dest.writeString(link);
        dest.writeByte((byte) (hot ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }
}
