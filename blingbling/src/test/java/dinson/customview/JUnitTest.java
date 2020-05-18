package dinson.customview;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String data = "8272585e8c47df06f690392b48f9117e1ad3471c2522a3380493a35dca8db71564e208c274fc0196b6a4591671d5dadc37342baaea7b461b1ccb25a89620ba3ed6bb2d0b9b7171ace210e25e56262aeb2eb65a617eb29d7e3503c8fc74d6fcd592bb9a5eeaa041971d3c6129316f888bce75e6c916ab0c06118e4c3f12ef6ca35b1c4b5f3c3d0db3dd020301cc5841ff7eccd94b99ff0727667e2aa63e59f83fd46db85607f59262f36750c375704762f5e7c00239549a0690a6770388bc4482f79198aca20f5e391dd3f6a94ebfd8a9315afdb5b23779380683cde67b3a631402e1bdb1669bc26dd9874c93961915bc454afc1e357212eb0de86fe467b045caee9924859dc8b902e786d9529ae59b17ca7839448c5fd8af47bb4fe7a489dd058b376693846fbc85fa3fbe5f1c450cc7c332297781ccc0c5cee7460f5070d80716ed1163a03872bf12cfc120c038aaca5c8e60f78dcbc8f273cac8b4e55d3657d4758803dfe04ad05364a9079d4c693d81c17b1301e080c9d8e3229437dca068cd68e033e51cba9dc8ecb0eb7fd04f4618b505b704b78c4199e6b4ad0b7a3ebac169e03f93b0d4f8158b1b66965d0ee21410d83381880a08c13eba647697d31a0a93926e0b4e26463685f1a619213df92c628ef8d6e441a14a963c59ba22db060b8440f308fb43ed26104791f7bb54a420f0ae97f1e86c3fbf2a56802408bd08565ee26e23ec626841a270e1925d9285122b0fe7260ad0a171128e8d40f3e62919f53b703eaa3481c931e1392d357f8b8e08480acb5e255b687504137c2da4e5009f56dd6600c725573e2415801c811f7a0b0aa3fa7eebcf2dfebacd570f5060daab1df40bfe4321ee1b6eed08235fc2b9ed96ee5229143f2ff494fbbc86c1db69502efe69e637508f8b564525855ba7107144ea88aa8818cb84de2048232cfe848d637dd7f56aad427f39a3133ba3ad3a514984a39da998096e8d6780563bbb09bb67076abc220988777ce2b4e55b0f79088b90fed19848881a006b35829db21161f979f94912810bb06c5f6f0ce8fac7c2ebf69c2afe8e944f88b50311bd2fbcb30f22edf3e5f3ee15363eea2fe5dc76bdc2fb2e1de17870a27577993298434cacdf34737084bf50bfdc672478b512cedb12035933b5e096f9d067ee12929d816cb72f89faaebb833d03af32628bf9e38bf1d92c1d43b0ab4495deede956c9e47b0699ef0b87eb53620dd87c303deb8c004ac0f6ab5d68f600e35b945703a8b961a6cced7ea7157821d9d04d372ac4472aa7740eebebbb95a5050f59a175169d0775d5a2affeea906fc70e0dedc0c6d813ead5b24b598189f2780257fb4711b6ef270689fee95c92f1096eb31ff6c5436692f19af18bca1d92a08d14154be8e05e9c4cb3ec49a4e83037116a70bc65c4ba756f5990c04e5efb37207d4e9297ef875c52d328606c3246fb789da99c1e85e8e244dc75aa04b235ecca3cf6bbfa9f1d3f5902ce6fd5c483bd0cccaac59ec6aaaf79e99b2a93f456e3382c4c39cdb1c9dc2845f714d47f3b2a21492d0717cc446c27bcb555eeac20647ceeacf1a47a1a4b6428ab8409a249f9b7514699744bbb8114a33901ab525f924b8dc63324f1929775f122da93a19e0ccdb6f791d3f9e4a85e0c0bb88f1ee3d7126a1d5281639ea6f6f723c506898b025d4420f50fefda5a52478092567d7acb2b35f7f93cd5f9ea77555580a6c6a0a8b77e8c2d03bacce96f37d4b57a3f76ba748041769f49024e218c60ba79c6e5141b57007a6cd0b5bc05df8c58e2c8d3a259e874a421955e0acf8447b7d9867a2bc6593100c47e4323501383ee42cc4a2f0157a80c4ea87f9de50187a6452301e0b4b07b805ac2e49944f454b4c23513b386b88e76a5ec6dac4a769e51f1f03e28668426fd03ea5866905e4e94e246749ff31a70b8f6151934047d123d5df0a7a1b4d409d476977af7941b1d187e20d5c2f58e6e8e219dbd3e1070aee58d3cd779a3efedef1f3653d18716911aeabc0a581cadc69a0fdecac7aad1f9c5efb96f0e149f1fe1129f856563081a74c35829ebea8dde07b6ede014a82ed559fd069529c90e8d4f50e758f65cf7e633eb372eea1d4fbcbcb59fa056b541c2d801bb377bd65eaefeb2305de6f79b1bec3d215bc926641ac4a3aa6569843b39d6a7ec2f8d147794a613080fc59dad7f51088f21378c2ee317edbd9ff6a72dc96cad56a20906c485e4ce29c510a4e423640ef57c3fe82d841844e785db2c809f2d15bd25d6847b83452e4bfe402d39a94cf246babe96a1def01e6605d8b548aaef198ab68142a972f72ecbe45062318c3f246d280714bfc564e2be92dbace8201f10a1c5f3c553ef1d8480f783e35536905a44f6722c72bed0ca3e9d2d40756d2025a4d772ffd290e0ce0b6b2c8ccbcdcc7930b6d35834ff5a0c905accdf1d2b706b11ff2ca931df1dbedcafa0a854f36f226406d51a6e4f09624ea38263b9bcba675758e12282d60ff61a67b1bd47ecbdc1904cd2c551c821d55e7d4cc958de4e015076f14b4a44e08c621a064c13230457096afe8c7bb8d70fe1ad6cf1e11025dcd2e73aff9ffc363ba4b670021d09dedd0be96fe5eb33819cad64d2d4bbb9918a7b6c4df7af2d0de386569a1ba36763f2fd5ff9a9a2c5c65b4f8deedcaeeff20118d3698bf396ec31af6a504c851c2163ca4c9c85b56ae1317fd281cdf3d87362440a821dd9d706e78f923e8098e3ad8fa77c776ac10fb50d5434ead778b938a87ef86a7a2ff034ecbe20d08b9f5ca60a87026da03b27a0574b3e7a8bfffde2c8e8a4b7c3a0752f2e1c2d0dbaae1a547fda8aeebfc7cc82faaddd6cd491b168cefb4838fd2523e0af826e67420d61fdf02a1b134e8186ecf21e49f8f6ed30d9c61fc1b9af440f630a46b26c117668d52647d2a45f77e74e945268003eb0b1d59be77ce1019a0f3b79";
        String key = "M7z8I9t0U";
        String iv = "0809040409090708";

        byte[] data2 = hexStr2Byte(data);
         String result =   decodeCBCSync(data2,key,iv);
        System.out.println("result :  "+result);
        /*/https:\/\/.*?(jpg|jpeg|png)/g*/

        System.out.println("-----------------------");
        result+="http:dd.jpeg,http://df.png";

        // 按指定模式在字符串查找
//        String pattern = "^https://.*?(jpg|jpeg|png)/$";
        String pattern = "http(.*?)(jpg|jpeg|png)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(result);
        int i = 0;
        // m.find 是否找到正则表达式中符合条件的字符串
        while (m.find( )) {
            System.out.println("Found value: " + m.group() );
            i++;
        }

        System.out.println("-----------------------");

        JSONMinify minify = new JSONMinify();
        String json="{\n" +
            "\t\"basic_url\": \"https://api.mmzztt.com/wp-json/wp/v2/\", //old_list\n" +
            "\t\"list_url\": \"https://adr.mmzztt.com/wp-json/wp/v2/\", //new_list\n" +
            "\t\"post_url\": \"https://app.mmzztt.com/wp-json/wp/v2/\", //post\n" +
            "\t\"update_url\": \"https://adr.mmzztt.com/app/update/version.json\",\n" +
            "\t\"new_update_url\": \"https://adr.mmzztt.com/app/update/new_version.json\",\n" +
            "\t\"z_url\": \"https://adr.mmzztt.com/json/x.json\",\n" +
            "\t\"block\": \"1\"\n" +
            "}";
        String sout = minify.minify(json);
        System.out.println(sout);


    }

    /**
     * 获取所有满足正则表达式的字符串
     * @param str 需要被获取的字符串
     * @param regex 正则表达式
     * @return 所有满足正则表达式的字符串
     */
    private ArrayList<String> getAllSatisfyStr(String str, String regex) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ArrayList<String> allSatisfyStr = new ArrayList<>();
        if (regex == null || regex.isEmpty()) {
            allSatisfyStr.add(str);
            return allSatisfyStr;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            allSatisfyStr.add(matcher.group());
        }
        return allSatisfyStr;
    }

    /**
     * 解密 带偏移量
     *
     * @param data   2进制流
     * @param key    秘钥
     * @param offset 偏移量
     * @return 明文
     */
    public static String decodeCBCSync(byte[] data, String key, String offset) throws Exception {
        byte[] key2 = toMakekey(key);
        byte[] iv = offset.getBytes();
        // 初始化
        //Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        Key secretKey = new SecretKeySpec(key2, "AES");
        // 初始化cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[]  encryptedText = cipher.doFinal(data);
        return new String(encryptedText, StandardCharsets.UTF_8);//此处使用BASE64做转码。
    }

    /**
     * 补足key 16的倍数位
     *
     * @param key 补足key
     * @return 补全后的key byte[]
     */
    private static byte[] toMakekey(String key) {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        byte[] keyBytes = key.getBytes();
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            return temp;
        } else return keyBytes;
    }


    /**
     * 16进制转2进制
     *
     * @param hex 16进制流
     * @return 2进制流
     */
    private byte[] hexStr2Byte(String hex) {
        ByteBuffer bf = ByteBuffer.allocate(hex.length() / 2);
        for (int i = 0; i < hex.length(); i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            bf.put(b);
        }
        return bf.array();
    }
    public class JSONMinify {
        public   String minify(String jsonString) {
            boolean in_string = false;
            boolean in_multiline_comment = false;
            boolean in_singleline_comment = false;
            char string_opener = 'x'; // unused value, just something that makes compiler happy

            StringBuilder out = new StringBuilder();
            for (int i = 0; i < jsonString.length(); i++) {
                // get next (c) and next-next character (cc)

                char c = jsonString.charAt(i);
                String cc = jsonString.substring(i, Math.min(i+2, jsonString.length()));

                // big switch is by what mode we're in (in_string etc.)
                if (in_string) {
                    if (c == string_opener) {
                        in_string = false;
                        out.append(c);
                    } else if (c == '\\') { // no special treatment needed for \\u, it just works like this too
                        out.append(cc);
                        ++i;
                    } else
                        out.append(c);
                } else if (in_singleline_comment) {
                    if (c == '\r' || c == '\n')
                        in_singleline_comment = false;
                } else if (in_multiline_comment) {
                    if (cc.equals("*/")) {
                        in_multiline_comment = false;
                        ++i;
                    }
                } else {
                    // we're outside of the special modes, so look for mode openers (comment start, string start)
                    if (cc.equals("/*")) {
                        in_multiline_comment = true;
                        ++i;
                    } else if (cc.equals("//")) {
                        in_singleline_comment = true;
                        ++i;
                    } else if (c == '"' || c == '\'') {
                        in_string = true;
                        string_opener = c;
                        out.append(c);
                    } else if (!Character.isWhitespace(c))
                        out.append(c);
                }
            }
            return out.toString();
        }
    }
}