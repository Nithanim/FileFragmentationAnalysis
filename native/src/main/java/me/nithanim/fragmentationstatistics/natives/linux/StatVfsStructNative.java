package me.nithanim.fragmentationstatistics.natives.linux;

import me.nithanim.fragmentationstatistics.natives.NativeLoader;

/**
 * The fstat struct https://linux.die.net/man/2/fstat
 */
public class StatVfsStructNative implements StatVfsStruct {
    static {
        NativeLoader.loadLibrary();
    }

    private long addr;

    public static StatVfsStructNative allocate() {
        return new StatVfsStructNative();
    }

    public StatVfsStructNative() {
        alloc();
    }

    private native void alloc();

    long getAddr() {
        return addr;
    }

    @Override
    public native long getPreferredBlocksize();

    @Override
    public native long getMinimumBlockSize();

    @Override
    public native long getTotalNumberOfBlocks();

    @Override
    public native long getNumberOfFreeBlocks();

    @Override
    public native long getNumberOfFreeBlocksUnprivileged();

    @Override
    public native long getNumberOfInodes();

    @Override
    public native long getNumberOfFreeInodes();

    @Override
    public native long getNumberOfFreeInodesUnprivileged();

    @Override
    public native long getFileSystemId();

    @Override
    public native long getMountFlags();

    @Override
    public native long getMaxFileNameLength();

    @Override
    public native void close() throws Exception;
}
