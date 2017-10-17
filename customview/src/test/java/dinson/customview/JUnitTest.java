package dinson.customview;

import org.junit.Test;

import dinson.customview.utils.StringUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JUnitTest {
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


//        String url = "https:www/.baidu.com123456.apk";
//        String baseUrl = StringUtils.getUrlName(url);
//        System.out.println("ba:" + baseUrl);



//        double a =;

        String s="1111111111111111111112222.2333333444333333333333333333333333333333333333333333333333333333";
        String[] split =s.split("\\.");
        System.out.println("len:"+split.length);
        if (split.length==1){
            System.out.println("1:"+split[0].length());
        }else{
            System.out.println("2:"+split[0].length()+" "+split[0]+" "+split[1].length()+" "+split[1]);
        }
    }

    static String formatMoney(float money) {
        return StringUtils.formatMoney(money);
    }
}