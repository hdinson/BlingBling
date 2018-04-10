package dinson.customview.utils;

import android.annotation.SuppressLint;

import java.util.Locale;

/**
 *
 */
public class ByteUtils {
    public static String byteTobyteString(byte[] bArray, boolean isInvert) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        if (isInvert == false) {
            for (int i = 0; i < bArray.length; ++i) {
                sTemp = Integer.toHexString(0xFF & bArray[i]);
                if (sTemp.length() < 2) {
                    sb.append(0);
                }
                sb.append(sTemp.toUpperCase(Locale.getDefault()));
            }
        } else {
            for (int i = bArray.length - 1; i >= 0; --i) {
                sTemp = Integer.toHexString(0xFF & bArray[i]);
                if (sTemp.length() < 2) {
                    sb.append(0);
                }
                sb.append(sTemp.toUpperCase(Locale.getDefault()));
            }
        }

        return sb.toString();
    }

    //byteString转换成byte
    @SuppressLint("DefaultLocale")
    public static byte[] byteStringTobyte(String hexString) {
        String chars = "0123456789ABCDEF";
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; ++i) {
            int pos = i * 2;
            d[i] = (byte) (chars.indexOf(hexChars[pos]) << 4 | chars.indexOf(hexChars[pos + 1]));
        }
        return d;
    }

}
