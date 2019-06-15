package dinson.customview.entity.av;

public class Actress {

   private String text;
    private  String avatarUrl;
    private  String href;

    public Actress(String text, String avatarUrl, String href) {
        this.text = text;
        this.avatarUrl = avatarUrl;
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
