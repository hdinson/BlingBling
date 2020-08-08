package com.dinson.blingbase.rxcache.diskconverter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * 通用转换器
 */
public interface IDiskConverter {

    /**
     * 读取
     */
    <T> T load(InputStream source, Type type);

    /**
     * 写入
     */
    boolean writer(OutputStream sink, Object data);

}
