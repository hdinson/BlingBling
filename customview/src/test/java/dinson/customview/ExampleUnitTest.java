package dinson.customview;

import org.junit.Test;

import dinson.customview.utils.StringUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

//        int[] ints = MainAdapter.formatPosition(1);
//        System.out.println(ints[0]+","+ints[1]+","+ints[2]);

//        String token = "qiniuphotos:gogopher.jpg"; // 编码前
//        String token = "hello world"; // 编码前
//        String  base64Token = Base64.encodeToString(token.getBytes(), Base64.DEFAULT);//  编码后
//
//         byte[] m = Base64.decode(base64Token,Base64.DEFAULT);// 解码后
//        System.out.println(m+"-->");

//        String encodedString = Base64.encodeToString("whoislcj".getBytes(), Base64.DEFAULT);
//        System.out.println( encodedString);



        String url="https:www/.baidu.com123456.apk";



        String baseUrl = StringUtils.getUrlName(url);

        System.out.println("ba:"+baseUrl);


    }



}