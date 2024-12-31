package dinson.customview.utils.haval;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {
    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    /**
     * 使用 RSA 公钥对字符串进行加密，并返回 Base64 编码的结果。
     *
     * @param base64PublicKey Base64 编码的公钥
     * @param data            待加密的字符串
     * @return 加密后的 Base64 编码字符串，或 null（加密失败时）
     */
    public static String encryptWithPublicKey(String base64PublicKey, String data) {
        try {
            PublicKey publicKey = generatePublicKey(base64PublicKey);
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return new String(Base64.encode(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT));
        } catch (Exception unused) {
            return null;
        }
    }

    /**
     * 根据 Base64 编码的字符串生成 RSA 公钥。
     *
     * @param base64Key Base64 编码的公钥字符串
     * @return 公钥对象
     * @throws NoSuchAlgorithmException 如果指定的算法不可用
     * @throws Exception                其他异常，如无效的密钥格式
     */
    private static PublicKey generatePublicKey(String base64Key) throws NoSuchAlgorithmException, Exception {
        return KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode(base64Key, 0)));
    }


}
