package me.nithanim.fragmentationstatistics.natives.windows;

import me.nithanim.fragmentationstatistics.natives.NativeLoader;

public class StartingVcnInputBufferNative implements StartingVcnInputBuffer {
    static {
        NativeLoader.loadLibrary();
    }

    public static StartingVcnInputBufferNative allocate() {
        return new StartingVcnInputBufferNative();
    }

    @java.lang.annotation.Native
    private long addr;

    public StartingVcnInputBufferNative() {
        _allocate();
    }

    private native void _allocate();

    @Override
    public native long getStartingVcn();

    @Override
    public native void setStartingVcn(long startingVcn);

    @Override
    public long getAddr() {
        return addr;
    }

    @Override
    public native int getStructSize();

    @Override
    public native void close();
}
