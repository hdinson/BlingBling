package dinson.customview.utils.haval;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DesUtils {


    public static CipherTextBean b(String pub, String str2) throws UnsupportedEncodingException {
        String str3;
        str3 = new String(Base64.decode(pub, Base64.NO_WRAP), "UTF-8");
        pub = str3.replace("-----BEGIN PUBLIC KEY-----\n", "").replace("\n-----END PUBLIC KEY-----", "");


        CipherTextBean cipherTextBean2 = new CipherTextBean();
        String sk1 = AES.generateRandomKeyAsHex();
        String iv1 = AES.generateRandomIV();
        try {
            String rsa = RSA.encryptWithPublicKey(pub, sk1 + "haval");
            String sk = URLEncoder.encode(rsa, "utf-8");
            cipherTextBean2.setAesSecretKey(sk);
            cipherTextBean2.setAesVector(URLEncoder.encode(RSA.encryptWithPublicKey(pub, iv1), "utf-8"));
            cipherTextBean2.setContent(AES.encryptString(str2, sk1, iv1));
            return cipherTextBean2;
        } catch (UnsupportedEncodingException e3) {
            e3.printStackTrace();
            return null;
        }
    }
}
