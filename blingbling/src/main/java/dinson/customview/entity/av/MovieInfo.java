package dinson.customview.entity.av;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MovieInfo   {
    private  String coverUrl;
    private ArrayList<Screenshot> screenshots = new ArrayList<>();//截图
    private ArrayList<Header> headers = new ArrayList<>();//制作商，系列等可跳转信息
    private ArrayList<Genre> genres = new ArrayList<>();//标签分类
    private ArrayList<Actress> actresses = new ArrayList<>();//演员

    @Override
    public String toString() {
        return "MovieInfo{" +
                ", screenshots=" + screenshots +
                ", headers=" + headers +
                ", genres=" + genres +
                ", actresses=" + actresses +
                '}';
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public ArrayList<Screenshot> getScreenshots() {
        return screenshots;
    }

    public ArrayList<Header> getHeaders() {
        return headers;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public ArrayList<Actress> getActresses() {
        return actresses;
    }

    public static class Screenshot {
        private String thumbnailUrl;
        private String link;

        public Screenshot(String thumbnailUrl, String link) {
            this.thumbnailUrl = thumbnailUrl;
            this.link = link;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getLink() {
            return link;
        }

        @Override
        public String toString() {
            return "Screenshot{" +
                    "thumbnailUrl='" + thumbnailUrl + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }

    public static class Header {
        private String name;
        private String value;
        private String link;

        public Header(String name, String value, String link) {
            this.name = name;
            this.value = value;
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getLink() {
            return link;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }

    //暂无分类信息
    public static class Genre implements Parcelable {
        private String name;
        private String link;

        public Genre(String name, String link) {
            this.name = name;
            this.link = link;
        }

        protected Genre(Parcel in) {
            name = in.readString();
            link = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(link);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Genre> CREATOR = new Creator<Genre>() {
            @Override
            public Genre createFromParcel(Parcel in) {
                return new Genre(in);
            }

            @Override
            public Genre[] newArray(int size) {
                return new Genre[size];
            }
        };

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        @Override
        public String toString() {
            return "Genre{" +
                    "name='" + name + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }

    //演员信息
    public static class Actress {
        private String name;
        private String imageUrl;
        private String link;

        public Actress(String name, String imageUrl, String link) {
            this.name = name;
            this.imageUrl = imageUrl;
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getLink() {
            return link;
        }

        @Override
        public String toString() {
            return "Actress{" +
                    "name='" + name + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }
}
