package dinson.customview.entity;

public class ClassBean {


    private String title;
    private String desc;
    private Class name;
    private String imgUrl;


    public ClassBean(String title,String desc, Class name, String imgUrl) {
        this.title=title;
        this.desc = desc;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Class getName() {
        return name;
    }

    public void setName(Class name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
