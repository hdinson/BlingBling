package dinson.customview.utils.steganography.model;

import android.graphics.Bitmap;


import dinson.customview.utils.AESUtils;

/**
 * This main class of the text steganography
 */
public class ImageSteganography {

    private String message;
    private String secretKey;
    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    /**
     * 加密图片
     *
     * @param message   密文
     * @param secretKey AES加密key
     * @param image     源图片
     */
    public ImageSteganography(String message, String secretKey, Bitmap image) {
        this.message = message;
        this.secretKey = secretKey;
        this.image = image;
    }

    /**
     * 解密图片
     *
     * @param secretKey AES加密key
     * @param image     源图片
     */
    public ImageSteganography(String secretKey, Bitmap image) {
        this.secretKey = secretKey;
        this.image = image;
    }

    public String encryptMessage() {
        try {
            return AESUtils.encrypt(this.secretKey, this.message);
        } catch (Exception e) {
            return this.message;
        }
    }

    public String decryptMessage(String message) {
        try {
            return AESUtils.decrypt(secretKey, message);
        } catch (Exception e) {
            return message;
        }
    }
}
