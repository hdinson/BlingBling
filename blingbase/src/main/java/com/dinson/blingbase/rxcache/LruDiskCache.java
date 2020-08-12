package com.dinson.blingbase.rxcache;

import com.dinson.blingbase.rxcache.diskconverter.IDiskConverter;
import com.dinson.blingbase.rxcache.disklrucache.DiskLruCache;
import com.dinson.blingbase.rxcache.utils.RxCacheLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class LruDiskCache {
    private IDiskConverter mDiskConverter;
    private DiskLruCache mDiskLruCache;


    public LruDiskCache(IDiskConverter diskConverter, File diskDir, int appVersion, long diskMaxSize) {
        this.mDiskConverter = diskConverter;
        try {
            mDiskLruCache = DiskLruCache.open(diskDir, appVersion, 2, diskMaxSize);
        } catch (IOException e) {
            RxCacheLog.Companion.getInstance().loge(e);
        }
    }

    public <T> CacheHolder<T> load(String key, Type type) {
        if (mDiskLruCache == null) {
            return null;
        }
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                InputStream source = snapshot.getInputStream(0);
                T value = mDiskConverter.load(source, type);
                String string = snapshot.getString(1);
                long timestamp = Long.parseLong(string);
                snapshot.close();
                return new CacheHolder<>(value, timestamp);
            }
        } catch (IOException e) {
            RxCacheLog.Companion.getInstance().loge(e);
        }
        return null;
    }


    public <T> boolean save(String key, T value) {
        if (mDiskLruCache == null) {
            return false;
        }
        //如果要保存的值为空,则删除
        if (value == null) {
            return remove(key);
        }
        DiskLruCache.Editor edit = null;
        try {
            edit = mDiskLruCache.edit(key);
            OutputStream sink = edit.newOutputStream(0);
            mDiskConverter.writer(sink, value);
            long l = System.currentTimeMillis();
            edit.set(1, String.valueOf(l));
            edit.commit();
            RxCacheLog.Companion.getInstance().logv("save:  value=" + value + " , status=" + true);
            return true;
        } catch (IOException e) {
            RxCacheLog.Companion.getInstance().loge(e);
            if (edit != null) {
                try {
                    edit.abort();
                } catch (IOException e1) {
                    RxCacheLog.Companion.getInstance().loge(e1);
                }
            }
            RxCacheLog.Companion.getInstance().logv("save:  value=" + value + " , status=" + false);
        }
        return false;
    }


    public boolean containsKey(String key) {
        if (mDiskLruCache != null) {
            try {
                return mDiskLruCache.get(key) != null;
            } catch (IOException e) {
                RxCacheLog.Companion.getInstance().loge(e);
            }
        }
        return false;
    }

    /**
     * 删除缓存
     */
    public boolean remove(String key) {
        if (mDiskLruCache != null) {
            try {
                return mDiskLruCache.remove(key);
            } catch (IOException e) {
                RxCacheLog.Companion.getInstance().loge(e);
            }
        }
        return false;
    }

    public void clear() throws IOException {
        if (mDiskLruCache != null) {
            deleteContents(mDiskLruCache.getDirectory());
        }
    }

    private static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }
}
