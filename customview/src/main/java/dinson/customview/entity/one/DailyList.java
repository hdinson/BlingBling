package dinson.customview.entity.one;

import java.util.List;

/**
 * Created by DINSON on 2017/7/2.
 */

public class DailyList {
    /**
     * res : 0
     * data : ["1756","1755","1754","1753","1752","1751","1750","1749","1748","1747"]
     */

    private int res;
    private List<Integer> data;

    @Override
    public String toString() {
        return "DailyList{" +
            "res=" + res +
            ", data=" + data.toString() +
            '}';
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
