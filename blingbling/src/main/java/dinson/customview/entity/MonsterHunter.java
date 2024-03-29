package dinson.customview.entity;

import java.util.List;

import com.dinson.blingbase.widget.recycleview.multitype.MultiType;

/**
 * 怪物猎人数据bean
 */
public class MonsterHunter {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {


        /**
         * family : 两生種
         * monster : [{"icon":"mh_ic_020.png","name":"化鲛"},{"icon":"http://ondlsj2sn.bkt.clouddn.com/mh_ic_012.png","name":"虎鲛","species":"化鲛亜種"},{"icon":"http://ondlsj2sn.bkt.clouddn.com/mh_ic_003.png","name":"旋齿鲨"},{"icon":"http://ondlsj2sn.bkt.clouddn.com/mh_ic_019.png","name":"鬼蛙"},{"icon":"http://ondlsj2sn.bkt.clouddn.com/mh_ic_021.png","name":"荒鬼蛙","species":"鬼蛙亜種"},{"icon":"http://ondlsj2sn.bkt.clouddn.com/mh_ic_006.png","name":"岩穿","species":"鬼蛙（二名特殊個体）"}]
         */

        private String family;
        private List<MonsterBean> monster;

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public List<MonsterBean> getMonster() {
            return monster;
        }

        public void setMonster(List<MonsterBean> monster) {
            this.monster = monster;
        }

        public static class MonsterBean {
            /**
             * icon : /mh_ic_020.png
             * name : 化鲛
             * species : 化鲛亜種
             */
            private String icon;
            private String name;
            private String species;

            private boolean isTitle = false;
            private String family;

            public String getFamily() {
                return family;
            }

            public void setFamily(String family) {
                this.family = family;
            }

            public MonsterBean(String family, boolean isTitle) {
                this.isTitle = isTitle;
                this.family = family;
            }

            public boolean isTitle() {
                return isTitle;
            }

            public void setTitle(boolean title) {
                isTitle = title;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSpecies() {
                return species;
            }

            public void setSpecies(String species) {
                this.species = species;
            }
        }
    }
}
