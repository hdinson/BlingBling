package com.dinson.blingbase.rxcache;

/**
 * 缓存目标
 */
public enum CacheTarget {
    Memory,
    Disk,
    MemoryAndDisk;

    public boolean supportMemory() {
        return this==Memory || this== MemoryAndDisk;
    }

    public boolean supportDisk() {
        return this==Disk || this== MemoryAndDisk;
    }

}
