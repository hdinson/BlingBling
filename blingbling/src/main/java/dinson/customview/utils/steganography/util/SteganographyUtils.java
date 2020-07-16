package dinson.customview.utils.steganography.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static dinson.customview.kotlin.LogExtentionKt.logd;

/*
This is the SteganographyUtils Class containing some useful methods
 */
public class SteganographyUtils {

    //分割块的个数
    private static final int SQUARE_BLOCK_SIZE = 512;

    /**
     * This method calculates the number of square block needed
     *
     * @param pixels number of pixels {Integer}
     * @return number of Square blocks {Integer}
     */
    public static int squareBlockNeeded(int pixels) {
        int quadratic = SQUARE_BLOCK_SIZE * SQUARE_BLOCK_SIZE;
        int divisor = pixels / (quadratic);
        int remainder = pixels % (quadratic);
        return divisor + (remainder > 0 ? 1 : 0);
    }

    /**
     * 该方法将图像分割成许多（SQUARE_BLOCK_SIZE * SQUARE_BLOCK_SIZE）大小的图像。
     *
     * @param bitmap {Bitmap}
     * @return List of splitted images {List}
     */
    public static List<Bitmap> splitImage(Bitmap bitmap) {

        //用于小图像块的高度和宽度
        int chunkHeight, chunkWidth;

        //在此列表中以位图格式存储所有小图像块
        ArrayList<Bitmap> chunkedImages = new ArrayList<>();

        //该矩阵的行和列
        int rows = bitmap.getHeight() / SQUARE_BLOCK_SIZE;
        int cols = bitmap.getWidth() / SQUARE_BLOCK_SIZE;

        int chunk_height_mod = bitmap.getHeight() % SQUARE_BLOCK_SIZE;
        int chunk_width_mod = bitmap.getWidth() % SQUARE_BLOCK_SIZE;

        if (chunk_height_mod > 0)
            rows++;
        if (chunk_width_mod > 0)
            cols++;


        //x_coordinate and y_coordinate are the pixel positions of the image chunks
        int y_coordinate = 0;

        for (int x = 0; x < rows; x++) {

            int x_coordinate = 0;

            for (int y = 0; y < cols; y++) {

                chunkHeight = SQUARE_BLOCK_SIZE;
                chunkWidth = SQUARE_BLOCK_SIZE;

                if (y == cols - 1 && chunk_width_mod > 0)
                    chunkWidth = chunk_width_mod;

                if (x == rows - 1 && chunk_height_mod > 0)
                    chunkHeight = chunk_height_mod;

                //Adding chunk images to the list
                chunkedImages.add(Bitmap.createBitmap(bitmap, x_coordinate, y_coordinate, chunkWidth, chunkHeight));
                x_coordinate += SQUARE_BLOCK_SIZE;

            }
            y_coordinate += SQUARE_BLOCK_SIZE;
        }

        //returning the list
        return chunkedImages;
    }

    /**
     * 此方法将所有块图像列表合并为一个单一图像
     *
     * @param images          List {Bitmap}
     * @param original_height Original Height {Integer}
     * @param original_width  Original Width {Integer}
     * @return : Merged Image {Bitmap}
     */
    public static Bitmap mergeImage(List<Bitmap> images, int original_height, int original_width) {

        //Calculating number of Rows and columns of that matrix
        int rows = original_height / SQUARE_BLOCK_SIZE;
        int cols = original_width / SQUARE_BLOCK_SIZE;

        int chunk_height_mod = original_height % SQUARE_BLOCK_SIZE;
        int chunk_width_mod = original_width % SQUARE_BLOCK_SIZE;

        if (chunk_height_mod > 0)
            rows++;
        if (chunk_width_mod > 0)
            cols++;

        //create a bitmap of a size which can hold the complete image after merging
        logd(() -> "Size width " + original_width + " size height " + original_height);
        Bitmap bitmap = Bitmap.createBitmap(original_width, original_height, Bitmap.Config.ARGB_4444);

        //Creating canvas
        Canvas canvas = new Canvas(bitmap);

        int count = 0;

        for (int irows = 0; irows < rows; irows++) {
            for (int icols = 0; icols < cols; icols++) {
                //Drawing all the chunk images of canvas
                canvas.drawBitmap(images.get(count), (SQUARE_BLOCK_SIZE * icols), (SQUARE_BLOCK_SIZE * irows), null);
                count++;
            }
        }

        //returning bitmap
        return bitmap;
    }

    /**
     * 此方法将字节数组转换为整数数组。
     *
     * @param b {the byte array}
     * @return Integer Array
     */

    public static int[] byteArrayToIntArray(byte[] b) {

        Log.v("Size byte array", b.length + "");

        int size = b.length / 3;

        Log.v("Size Int array", size + "");

        System.runFinalization();
        //Garbage collection
        System.gc();

        Log.v("FreeMemory", Runtime.getRuntime().freeMemory() + "");
        int[] result = new int[size];
        int offset = 0;
        int index = 0;

        while (offset < b.length) {
            result[index++] = byteArrayToInt(b, offset);
            offset = offset + 3;
        }

        return result;
    }

    /**
     * Convert the byte array to an int.
     *
     * @param b {the byte array}
     * @return Integer
     */
    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    /**
     * 将字节数组转换为从给定偏移量开始的int值。
     *
     * @param b      the byte array
     * @param offset the byte array
     * @return Integer
     */
    private static int byteArrayToInt(byte[] b, int offset) {
        int value = 0x00000000;
        for (int i = 0; i < 3; i++) {
            int shift = (3 - 1 - i) * 8;
            value |= (b[i + offset] & 0x000000FF) << shift;
        }
        value = value & 0x00FFFFFF;
        return value;
    }

    /**
     * 将表示[argb]值的整数数组转换为字节数组
     *
     * @param array Integer array representing [argb] values.
     * @return Array representing [rgb] values.
     */
    public static byte[] convertArray(int[] array) {

        byte[] newarray = new byte[array.length * 3];

        for (int i = 0; i < array.length; i++) {

            newarray[i * 3] = (byte) ((array[i] >> 16) & 0xFF);
            newarray[i * 3 + 1] = (byte) ((array[i] >> 8) & 0xFF);
            newarray[i * 3 + 2] = (byte) ((array[i]) & 0xFF);

        }

        return newarray;
    }

}
