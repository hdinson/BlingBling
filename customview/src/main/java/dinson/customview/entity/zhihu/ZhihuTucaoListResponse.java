package dinson.customview.entity.zhihu;

import java.util.List;

/**
 * 知乎瞎扯服务器返回response
 */
public class ZhihuTucaoListResponse {

    /**
     * timestamp : 1519513198
     * stories : [{"images":["https://pic1.zhimg.com/v2-bbb227a4e2e284bbebb8bd8f8d5b89a0.jpg"],"date":"20180316","display_date":"3 月 16 日","id":9674013,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic4.zhimg.com/v2-da9410a0a0d4638a3a84d90c40ffd377.jpg"],"date":"20180315","display_date":"3 月 15 日","id":9673382,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic3.zhimg.com/v2-32d5552f747d0e41f5f6009f6ce64c16.jpg"],"date":"20180314","display_date":"3 月 14 日","id":9673638,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic2.zhimg.com/v2-7a2748b8b3fd3df3d71d87b9d24ef97d.jpg"],"date":"20180313","display_date":"3 月 13 日","id":9673439,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic1.zhimg.com/v2-e54b8bac112399e0231a2d2106aa0b9c.jpg"],"date":"20180312","display_date":"3 月 12 日","id":9673182,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic2.zhimg.com/v2-d0f615fd707a1155a96776b741ed83cd.jpg"],"date":"20180311","display_date":"3 月 11 日","id":9673175,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic4.zhimg.com/v2-46442d29c3e2b1adfc6f6029fb5f73d3.jpg"],"date":"20180310","display_date":"3 月 10 日","id":9672917,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic4.zhimg.com/v2-4004733b92ef47e8e003aafb11d99bb7.jpg"],"date":"20180309","display_date":"3 月 9 日","id":9672678,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic1.zhimg.com/v2-39563559269b9d1282d9c9dbb06e1cf0.jpg"],"date":"20180308","display_date":"3 月 8 日","id":9672614,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic4.zhimg.com/v2-7d6bfbd725a86f84384442293d82d9db.jpg"],"date":"20180307","display_date":"3 月 7 日","id":9672425,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic3.zhimg.com/v2-9b764d5e340203c7e8c0730979f6f45e.jpg"],"date":"20180306","display_date":"3 月 6 日","id":9672301,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic3.zhimg.com/v2-435703356edca5deabd3f5ee10b9f506.jpg"],"date":"20180305","display_date":"3 月 5 日","id":9672145,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic1.zhimg.com/v2-6b762f5e3af036ba20514ec8d463c6f0.jpg"],"date":"20180304","display_date":"3 月 4 日","id":9671926,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic1.zhimg.com/v2-eb19c0bbbae74b3ddaaf134c3a0f8060.jpg"],"date":"20180303","display_date":"3 月 3 日","id":9671917,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic1.zhimg.com/v2-24a2216e38c31174f938dcf0c56e92fc.jpg"],"date":"20180302","display_date":"3 月 2 日","id":9671903,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic4.zhimg.com/v2-aa607f2ee5484a327afecd0972f87e6f.jpg"],"date":"20180301","display_date":"3 月 1 日","id":9671892,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic1.zhimg.com/v2-ffe068ed37508f036324ec14c51e24b8.jpg"],"date":"20180228","display_date":"2 月 28 日","id":9671354,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic1.zhimg.com/v2-bd8367935573a5faf2e5ed754b66266c.jpg"],"date":"20180227","display_date":"2 月 27 日","id":9671289,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic2.zhimg.com/v2-cde9ca6116f0be70283aa485408239f1.jpg"],"date":"20180226","display_date":"2 月 26 日","id":9671164,"title":"瞎扯 · 如何正确地吐槽"},{"images":["https://pic4.zhimg.com/v2-8cfc46f5a46104fe634290fe1f35faeb.jpg"],"date":"20180225","display_date":"2 月 25 日","id":9671065,"title":"瞎扯 · 如何正确地吐槽"}]
     * name : 瞎扯
     */

    private int timestamp;
    private String name;
    private List<StoriesBean> stories;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic1.zhimg.com/v2-bbb227a4e2e284bbebb8bd8f8d5b89a0.jpg"]
         * date : 20180316
         * display_date : 3 月 16 日
         * id : 9674013
         * title : 瞎扯 · 如何正确地吐槽
         */

        private String date;
        private String display_date;
        private int id;
        private String title;
        private List<String> images;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDisplay_date() {
            return display_date;
        }

        public void setDisplay_date(String display_date) {
            this.display_date = display_date;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
