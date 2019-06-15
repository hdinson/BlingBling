package dinson.customview.entity.one;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DINSON on 2017/7/2.
 */

public class DailyDetail {
    /**
     * res : 0
     * data : {"hpcontent_id":"1736","hp_title":"VOL.1711","author_id":"-1","hp_img_url":"http://image.wufazhuce
     * .com/FlGKn4KQLvns6vJB5IwPoeE9y845","hp_img_original_url":"http://image.wufazhuce
     * .com/FlGKn4KQLvns6vJB5IwPoeE9y845","hp_author":"绘画＆Hammer Chen大木耳 作品","ipad_url":"http://image.wufazhuce
     * .com/FlGKn4KQLvns6vJB5IwPoeE9y845","hp_content":"回忆和期待一样，是一种简化和剪辑现实的工具。\r\n by 阿兰·德波顿",
     * "hp_makettime":"2017-06-13 06:00:00","hide_flag":"0","last_update_date":"2017-06-12 00:07:01",
     * "web_url":"http://m.wufazhuce.com/one/1736","wb_img_url":"","image_authors":"Hammer Chen大木耳",
     * "text_authors":"阿兰·德波顿","image_from":"","text_from":"","content_bgcolor":"#FFFFFF","template_category":"0",
     * "maketime":"2017-06-13 06:00:00","share_list":{"wx":{"title":"","desc":"","link":"http://m.wufazhuce
     * .com/one/1736?channel=singlemessage","img":"","audio":""},"wx_timeline":{"title":"","desc":"",
     * "link":"http://m.wufazhuce.com/one/1736?channel=timeline","img":"","audio":""},"weibo":{"title":"ONE一个
     * 回忆和期待一样，是一种简化和剪辑现实的工具。\r\n by 阿兰·德波顿\u2014\u2014阿兰·德波顿 下载ONE一个APP:http://weibo.com/p/100404157874","desc":"",
     * "link":"http://m.wufazhuce.com/one/1736?channel=weibo","img":"","audio":""},"qq":{"title":"","desc":"",
     * "link":"http://m.wufazhuce.com/one/1736?channel=qq","img":"","audio":""}},"praisenum":12825,
     * "sharenum":3168,"commentnum":0}
     */
    private DataBean data;
    private boolean isLocalCache = false;

    public boolean isLocalCache() {
        return isLocalCache;
    }

    public void setLocalCache(boolean localCache) {
        isLocalCache = localCache;
    }

    @Override
    public String toString() {
        return "DailyDetail{ " + data + '}';
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * hpcontent_id : 1736
         * hp_title : VOL.1711
         * author_id : -1
         * hp_img_url : http://image.wufazhuce.com/FlGKn4KQLvns6vJB5IwPoeE9y845
         * hp_img_original_url : http://image.wufazhuce.com/FlGKn4KQLvns6vJB5IwPoeE9y845
         * hp_author : 绘画＆Hammer Chen大木耳 作品
         * ipad_url : http://image.wufazhuce.com/FlGKn4KQLvns6vJB5IwPoeE9y845
         * hp_content : 回忆和期待一样，是一种简化和剪辑现实的工具。
         * by 阿兰·德波顿
         * hp_makettime : 2017-06-13 06:00:00
         * hide_flag : 0
         * last_update_date : 2017-06-12 00:07:01
         * web_url : http://m.wufazhuce.com/one/1736
         * wb_img_url :
         * image_authors : Hammer Chen大木耳
         * text_authors : 阿兰·德波顿
         * image_from :
         * text_from :
         * content_bgcolor : #FFFFFF
         * template_category : 0
         * maketime : 2017-06-13 06:00:00
         * share_list : {"wx":{"title":"","desc":"","link":"http://m.wufazhuce.com/one/1736?channel=singlemessage",
         * "img":"","audio":""},"wx_timeline":{"title":"","desc":"","link":"http://m.wufazhuce
         * .com/one/1736?channel=timeline","img":"","audio":""},"weibo":{"title":"ONE一个 回忆和期待一样，是一种简化和剪辑现实的工具。\r\n
         * by 阿兰·德波顿\u2014\u2014阿兰·德波顿 下载ONE一个APP:http://weibo.com/p/100404157874","desc":"","link":"http://m
         * .wufazhuce.com/one/1736?channel=weibo","img":"","audio":""},"qq":{"title":"","desc":"","link":"http://m
         * .wufazhuce.com/one/1736?channel=qq","img":"","audio":""}}
         * praisenum : 12825
         * sharenum : 3168
         * commentnum : 0
         */

        private String hpcontent_id;
        private String hp_img_url;
        private String hp_content;

        protected DataBean(Parcel in) {
            hpcontent_id = in.readString();
            hp_img_url = in.readString();
            hp_content = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(hpcontent_id);
            dest.writeString(hp_img_url);
            dest.writeString(hp_content);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        @Override
        public String toString() {
            return "hpcontent_id='" + hpcontent_id + '\'' +
                ", hp_img_url='" + hp_img_url + '\'';
        }

        public String getHpcontent_id() {
            return hpcontent_id;
        }

        public String getHp_img_url() {
            return hp_img_url;
        }

        public String getHp_content() {
            return hp_content;
        }
    }
}
