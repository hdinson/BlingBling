package dinson.customview.utils.haval;

import java.security.MessageDigest;
public class SecurityUtils {
    private static final String MD5_ALGORITHM = "MD5";

    /**
     * 生成输入字符串的 MD5 哈希值，并返回其十六进制字符串表示。
     *
     * @param input 输入字符串
     * @return MD5 哈希值的十六进制字符串，或原字符串（在发生异常时）
     */
    public static String generateMD5Hash(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        try {
            // 获取 MD5 摘要实例
            MessageDigest messageDigest = MessageDigest.getInstance(MD5_ALGORITHM);
            byte[] digest = messageDigest.digest(input.getBytes("UTF-8"));

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    hexString.append('0'); // 补齐一位
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return input; // 返回原字符串以防止程序崩溃
        }
    }
}
