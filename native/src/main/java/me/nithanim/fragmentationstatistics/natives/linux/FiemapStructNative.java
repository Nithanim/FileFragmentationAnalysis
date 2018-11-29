package me.nithanim.fragmentationstatistics.natives.linux;

import java.lang.annotation.Native;
import me.nithanim.fragmentationstatistics.natives.NativeLoader;

public class FiemapStructNative implements FiemapStruct {
    static {
        NativeLoader.loadLibrary();
    }

    public static FiemapStructNative allocate(int maxExtents) {
        return new FiemapStructNative(maxExtents);
    }

    @Native
    private final long addr;
    private final int maxExtents;

    private FiemapStructNative(int maxExtents) {
        long a = alloc(maxExtents);
        this.addr = a;
        this.maxExtents = maxExtents;
    }

    private native long alloc(int maxExtents);

    @Override
    public native void setStart(long offset);

    @Override
    public native void setLength(long length);

    @Override
    public native void setFlags(int flags);

    @Override
    public native int getFlags();

    @Override
    public native int getMappedExtents();

    @Override
    public FiemapExtent getExtent(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Index must be greater or equal 0!");
        }
        if (i >= maxExtents) {
            throw new IndexOutOfBoundsException("Requested extent " + i + " but array is only " + maxExtents + " long!");
        }
        return new FiemapExtentNative(getExtentAddr(i));
    }

    private native long getExtentAddr(int i);

    @Override
    public native void close();

    long getAddr() {
        return addr;
    }
}
