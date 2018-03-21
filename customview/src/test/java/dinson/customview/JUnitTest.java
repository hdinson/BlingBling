package dinson.customview;

import org.junit.Test;

import java.util.regex.Pattern;

import dinson.customview.utils.AESUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        /*ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;

            fixedThreadPool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
        /*Random random = new Random();
        System.out.println(random.nextInt());
        System.out.println(random.nextInt());
        System.out.println(random.nextInt());
        System.out.println(random.nextInt());
        System.out.println(random.nextInt());*/

        String str = "jtef:/dfas[23ajfkav8293.12!@#$%^&*()_+}{:ha";
        System.out.println(str);


        String code = AESUtils.encrypt("YWJjZGVmZ2hpamt", str);
        System.out.println(code);

        String code2 = AESUtils.decrypt("YWJjZGVmZ2hpamt", code);
        System.out.println(code2);
    }

    /**
     * 是否以数字结尾
     */
    private boolean isEndWithNum(String str) {
        boolean matches = Pattern.matches("(.*\\d+$)", str);
        return matches;
    }
}