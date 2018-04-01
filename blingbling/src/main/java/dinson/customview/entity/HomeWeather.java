package dinson.customview.entity;

import java.util.List;

/**
 * @author Dinson - 2017/9/14
 */
public class HomeWeather {
    @Override
    public String toString() {
        return "XinZhiWeather{" +
            "name=" + results.get(0).getLocation().name +
            ", weather=" + results.get(0).getNow().getText() +
            ", temperature=" + results.get(0).getNow().getTemperature() +
            '}';
    }

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * location : {"id":"WSKMS3KBE2JM","name":"泉州","country":"CN","path":"泉州,泉州,福建,中国",
         * "timezone":"Asia/Shanghai","timezone_offset":"+08:00"}
         * now : {"text":"多云","code":"4","temperature":"32"}
         * last_update : 2017-09-14T17:00:00+08:00
         */

        private LocationBean location;
        private NowBean now;
        private String last_update;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public NowBean getNow() {
            return now;
        }

        public void setNow(NowBean now) {
            this.now = now;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }

        public static class LocationBean {
            /**
             * id : WSKMS3KBE2JM
             * name : 泉州
             * country : CN
             * path : 泉州,泉州,福建,中国
             * timezone : Asia/Shanghai
             * timezone_offset : +08:00
             */

            private String id;
            private String name;
            private String country;
            private String path;
            private String timezone;
            private String timezone_offset;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getTimezone() {
                return timezone;
            }

            public void setTimezone(String timezone) {
                this.timezone = timezone;
            }

            public String getTimezone_offset() {
                return timezone_offset;
            }

            public void setTimezone_offset(String timezone_offset) {
                this.timezone_offset = timezone_offset;
            }
        }

        public static class NowBean {
            /**
             * text : 多云
             * code : 4
             * temperature : 32
             */

            private String text;
            private int code;
            private String temperature;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }
        }
    }
}
