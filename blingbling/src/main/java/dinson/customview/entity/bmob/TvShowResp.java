package dinson.customview.entity.bmob;

import java.util.List;

public class TvShowResp {

    private int code;
    private String error;

    private List<TvShow> results;

    public List<TvShow> getResults() {
        return results;
    }

    public void setResults(List<TvShow> results) {
        this.results = results;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class TvShow {
        /**
         * objectId : d4Xz111K
         * showOrder : 1
         * tvName : 泉州1套
         * tvUrl : http://live.qztvxwgj.com/live/news.m3u8
         * updatedAt : 2019-02-27 19:45:19
         */

        private int showOrder = Integer.MAX_VALUE;
        private String tvName;
        private String tvUrl;
        private String tvIcon;
        private String updatedAt;

        public String getTvIcon() {
            return tvIcon;
        }

        public void setTvIcon(String tvIcon) {
            this.tvIcon = tvIcon;
        }


        public int getShowOrder() {
            return showOrder;
        }

        public void setShowOrder(int showOrder) {
            this.showOrder = showOrder;
        }

        public String getTvName() {
            return tvName;
        }

        public void setTvName(String tvName) {
            this.tvName = tvName;
        }

        public String getTvUrl() {
            return tvUrl;
        }

        public void setTvUrl(String tvUrl) {
            this.tvUrl = tvUrl;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
