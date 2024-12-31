package dinson.customview.utils.haval;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS7Padding";

    /**
     * 生成随机的 IV 参数（初始向量）并返回其十六进制字符串表示。
     */
    public static String generateRandomIV() {
        byte[] bArr = new byte[8];
        new SecureRandom().nextBytes(bArr);
        return bytesToHex(new IvParameterSpec(bArr).getIV());
    }

    /**
     * 生成随机的 AES 密钥（128 位）。
     */
    public static SecretKeySpec generateRandomKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom());
            return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成随机密钥并返回其十六进制字符串表示。
     */
    public static String generateRandomKeyAsHex() {
        return bytesToHex(generateRandomKey().getEncoded());
    }

    /**
     * 根据给定字符串生成固定的 AES 密钥（16 字节）。
     */
    public static SecretKeySpec createKeyFromString(String str) {
        byte[] bArr;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bArr = null;
        }
        return new SecretKeySpec(bArr, "AES");
    }

    /**
     * 根据给定字符串生成固定的 IV 参数（16 字节）。
     */
    private static IvParameterSpec createIVFromString(String str) {
        byte[] bArr;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bArr = null;
        }
        return new IvParameterSpec(bArr);
    }
    /**
     * 使用指定密钥和 IV 对字节数组进行 AES 加密。
     */
    public static byte[] encryptBytes(byte[] bArr, String str, String str2) {
        try {
            SecretKeySpec b = createKeyFromString(str);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(1, b, createIVFromString(str2));
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 使用指定密钥和 IV 对字符串进行 AES 加密，并返回 Base64 编码结果。
     */
    public static String encryptString(String str, String str2, String str3) {
        byte[] bArr;
        try {
            bArr = str.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            bArr = null;
        }
        return Base64.encodeToString(encryptBytes(bArr, str2, str3), 0);
    }

    public static String bytesToHex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString().toUpperCase();
    }
}
