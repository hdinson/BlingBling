package dinson.customview.entity.gank;

public class MeiZiTuUrl {


    /**
     * basic_url : https://api.meizitu.net/wp-json/wp/v2/
     * post_url : https://ios.meizitu.net/wp-json/wp/v2/
     * update_url : https://apk.meizitu.net/app/version.json
     * z_url : https://api.meizitu.net/json/x.json
     * block : 1
     */

    /*
    update_url : https://apk.meizitu.net/app/version.json
    {
    "version": "1.5.6",
    "title": "发现新版本",
    "tips": "修复已知问题。\n",
    "url": "https://apk.meizitu.net/app/mzitu_1.5.6.apk",
    "forgetpswtips": "\n\n重置密码: Z"
}
*/

    private String basic_url;
    private String post_url;
    private String z_url;

    public String getBasic_url() {
        return basic_url;
    }

    public void setBasic_url(String basic_url) {
        this.basic_url = basic_url;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getZ_url() {
        return z_url;
    }

    public void setZ_url(String z_url) {
        this.z_url = z_url;
    }
}
