package dinson.customview;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class XunLei {
    @Test
    public void encode() throws Exception {

        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = "QUFtYWduZXQ6P3h0PXVybjpidGloOjYwRENDQzBGNjEyQUI1NTFFRjhCREQxREU2RkJGRjZEMzc4NTFEQkRaWg==";
        final byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        //编码
        final String encodedText = encoder.encodeToString(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(decoder.decode(text),"GBK"));
    }
}