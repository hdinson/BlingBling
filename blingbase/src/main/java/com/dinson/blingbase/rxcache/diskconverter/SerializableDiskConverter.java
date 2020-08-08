package com.dinson.blingbase.rxcache.diskconverter;

import com.dinson.blingbase.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;


public class SerializableDiskConverter implements IDiskConverter {

    @Override
    public <T> T load(InputStream source, Type type) {
        T value = null;
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(source);
            value = (T) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(oin);
        }
        return value;
    }

    @Override
    public boolean writer(OutputStream sink, Object data) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(sink);
            oos.writeObject(data);
            oos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(oos);
        }
        return false;
    }

}
