package dinson.customview.entity.gank;

public class Welfare {
    /**
     * _id : 5b74e9409d21222c52ae4cb4
     * createdAt : 2018-08-16T11:02:24.289Z
     * desc : 2018-08-16
     * publishedAt : 2018-08-16T00:00:00.0Z
     * source : api
     * type : 福利
     * url : https://ws1.sinaimg.cn/large/0065oQSqly1fubd0blrbuj30ia0qp0yi.jpg
     * used : true
     * who : lijinshan
     */

    private String url;
    private String desc;

    private int height;//图片的高
    private int width;

    private String link;//仅妹子图网站需要
    private String refer;//仅妹子图网站需要

    public Welfare(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }
}
