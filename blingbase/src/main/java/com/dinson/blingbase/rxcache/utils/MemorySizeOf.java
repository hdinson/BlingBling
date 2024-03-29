package com.dinson.blingbase.rxcache.utils;

import android.graphics.Bitmap;

import com.dinson.blingbase.utils.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Implements functions useful to check
 * MemorySizeOf usage.
 *
 * @author Pierre Malarme
 * @version 1.0
 */
public class MemorySizeOf {

    /**
     * Function that get the size of an object.
     *
     * @return Size in bytes of the object or -1 if the object
     * is null.
     */
    public static int sizeOf(Serializable serial) throws IOException {
        if (serial == null) {
            return 0;
        }
        int size;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(serial);
            oos.flush();  //缓冲流
            size = baos.size();
        } finally {
            IOUtils.close(oos, baos);
        }
        return size;
    }


    public static int sizeOf(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }

        int size;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            size = baos.size();
        } finally {
            IOUtils.close(baos);
        }
        return size;
    }

}















