package dinson.customview;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import dinson.customview.utils.ArithmeticUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {


       String dinson="";
        System.out.println(isEndWithNum(dinson));

        BigDecimal x = new BigDecimal("0.11");
        System.out.println(x);
    }

    /**
     * 是否以数字结尾
     */
    private boolean isEndWithNum(String str) {
        boolean matches = Pattern.matches("(.*\\d+$)", str);
        return matches;
    }
}