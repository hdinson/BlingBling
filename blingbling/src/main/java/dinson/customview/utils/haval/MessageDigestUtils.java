package dinson.customview.utils.haval;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import kotlin.text.Charsets;

public final class MessageDigestUtils {

    @NotNull
    public static final MessageDigestUtils a = new MessageDigestUtils();

    private MessageDigestUtils() {
    }

    @NotNull
    public final String a(@NotNull String str) throws NoSuchAlgorithmException {
        Log.e(str, "str");
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        Log.d(bytes.toString(), "this as java.lang.String).getBytes(charset)");
        byte[] digest = messageDigest.digest(bytes);
        Log.d(digest.toString(), "result");
        return a(digest);
    }

    @NotNull
    public final String b(@NotNull String str) throws NoSuchAlgorithmException {
        Log.e(str, "str");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        Log.d(Arrays.toString(bytes), "this as java.lang.String).getBytes(charset)");
        byte[] digest = messageDigest.digest(bytes);
        Log.d(digest.toString(), "result");
        return a(digest);
    }

    @NotNull
    public final String c(@NotNull String str) throws NoSuchAlgorithmException {
        Log.e(str, "str");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        Log.d(bytes.toString(), "this as java.lang.String).getBytes(charset)");
        byte[] digest = messageDigest.digest(bytes);
        Log.d(digest.toString(), "result");
        return a(digest);
    }

    @NotNull
    public final String a(@NotNull byte[] bArr) {
        Log.e(bArr.toString(), "byteArray");
        StringBuilder sb = new StringBuilder();
        int length = bArr.length;
        int i = 0;
        while (i < length) {
            byte b = bArr[i];
            i++;
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                sb.append("0");
                sb.append(hexString);
            } else {
                sb.append(hexString);
            }
        }
        String sb2 = sb.toString();
        Log.d(sb2, "with(StringBuilder()) {\n            byteArray.forEach {\n                val hex = it.toInt() and (0xFF)\n                val hexStr = Integer.toHexString(hex)\n                if (hexStr.length == 1) {\n                    this.append(\"0\").append(hexStr)\n                } else {\n                    this.append(hexStr)\n                }\n            }\n            this.toString()\n        }");
        return sb2;
    }
}
