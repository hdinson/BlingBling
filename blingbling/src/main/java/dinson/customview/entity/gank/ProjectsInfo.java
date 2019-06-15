package dinson.customview.entity.gank;

import java.util.List;

import dinson.customview.weight.recycleview.multitype.MultiType;

public class ProjectsInfo implements MultiType {

    @Override
    public String toString() {
        return "ProjectsInfo{" +
            "desc='" + desc + '\'' +
            '}';
    }

    /**
     * _id : 5b7292189d212275a12274e5
     * createdAt : 2018-08-14T16:26:00.223Z
     * desc : 一个可爱的时钟view
     * images : ["https://ww1.sinaimg.cn/large/0073sXn7ly1fubd70msdgj307i07074h","https://ww1.sinaimg.cn/large/0073sXn7ly1fubd71dioig304e04qaab","https://ww1.sinaimg.cn/large/0073sXn7ly1fubd7dqhueg304e04qmxb","https://ww1.sinaimg.cn/large/0073sXn7ly1fubd7e0lb7g304e04rati"]
     * publishedAt : 2018-08-16T00:00:00.0Z
     * source : web
     * type : Android
     * url : https://github.com/samlss/ClockView
     * used : true
     * who : samlss
     */




    private String desc;
    private String publishedAt;
    private String type;
    private String url;
    private List<String> images;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
