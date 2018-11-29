package me.nithanim.fragmentationstatistics.natives.linux;

import me.nithanim.fragmentationstatistics.natives.NativeLoader;

/**
 * The fstat struct https://linux.die.net/man/2/fstat
 */
public class StatStruct implements AutoCloseable {
    static {
        NativeLoader.loadLibrary();
    }

    private long addr;

    public static StatStruct allocate() {
        return new StatStruct();
    }

    public StatStruct() {
        alloc();
    }

    private native void alloc();

    /**
     * ID of device containing file.
     */
    public native long getDev();

    /**
     * inode number
     */
    public native long getInodeNumber();

    /**
     * protection
     */
    public native short getMode();

    /**
     * number of hard links
     */
    public native int getNumberHardlinks();

    /**
     * user ID of owner
     */
    public native int getUid();

    /**
     * group ID of owner
     */
    public native int getGid();

    /**
     * device ID (if special file)
     */
    public native long getRdev();

    /**
     * total size, in bytes
     */
    public native long getSize();

    /**
     * blocksize for file system I/O
     */
    public native int getBlksize();

    /**
     * number of 512B blocks allocated
     */
    public native long getBlocks();

    /**
     * time of last access
     */
    public native long getATime();

    /**
     * time of last modification
     */
    public native long getMTime();

    /**
     * time of last status change
     */
    public native long getCTime();

    @Override
    public native void close() throws Exception;

    long getAddr() {
        return addr;
    }
}
