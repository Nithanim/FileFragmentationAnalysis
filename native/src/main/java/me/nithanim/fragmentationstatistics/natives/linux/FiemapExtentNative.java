package me.nithanim.fragmentationstatistics.natives.linux;

import java.lang.annotation.Native;
import me.nithanim.fragmentationstatistics.natives.NativeLoader;

public class FiemapExtentNative implements FiemapExtent {
    static {
        NativeLoader.loadLibrary();
    }
    
    @Native
    private final long addr;

    FiemapExtentNative(long addr) {
        this.addr = addr;
    }

    @Override
    public native long getLogical();

    @Override
    public native long getPhysical();

    @Override
    public native long getLength();

    @Override
    public native int getFlags();
}
