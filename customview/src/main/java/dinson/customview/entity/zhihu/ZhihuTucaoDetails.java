package dinson.customview.entity.zhihu;


import java.util.ArrayList;

public class ZhihuTucaoDetails {

    private String body;
    private ArrayList<String> css;
    private Long id;
    private String image;
    private String title;
    private ArrayList<String> images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<String> getCss() {
        return css;
    }

    public void setCss(ArrayList<String> css) {
        this.css = css;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
