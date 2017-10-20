package dinson.customview;

import org.junit.Test;

import java.util.regex.Pattern;

import io.reactivex.Observable;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        Observable.range(1,5).subscribe(integer -> {
            System.out.println(" "+integer);
        });



    }

    /**
     * 是否以数字结尾
     */
    private boolean isEndWithNum(String str) {
        boolean matches = Pattern.matches("(.*\\d+$)", str);
        return matches;
    }
}