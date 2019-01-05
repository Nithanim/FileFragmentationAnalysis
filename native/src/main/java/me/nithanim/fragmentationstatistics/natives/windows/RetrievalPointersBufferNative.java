package me.nithanim.fragmentationstatistics.natives.windows;

import me.nithanim.fragmentationstatistics.natives.NativeLoader;

public class RetrievalPointersBufferNative implements RetrievalPointersBuffer {
    static {
        NativeLoader.loadLibrary();
    }

    private static native int getStructSize(int nExtents);

    static RetrievalPointersBufferNative allocate(int nExtents) {
        if (nExtents < 1) {
            throw new IllegalArgumentException("Minimal number of extents is 1!");
        }
        return new RetrievalPointersBufferNative(nExtents);
    }

    private final int nAllocatedExtents;

    @java.lang.annotation.Native
    private long addr;

    RetrievalPointersBufferNative(int nExtents) {
        this.nAllocatedExtents = nExtents;
        _allocate(nExtents);
    }

    private native void _allocate(int nExtents);

    @Override
    public native int getExtentCount();

    @Override
    public native long getStartingVcn();

    @Override
    public ExtentNative getExtent(int idx) {
        if (idx < 0 || idx > nAllocatedExtents) {
            throw new ArrayIndexOutOfBoundsException("Must be between 0 and " + nAllocatedExtents + " but got " + idx);
        }
        return new ExtentNative(getExtentAddr(idx));
    }

    private native long getExtentAddr(int idx);

    @Override
    public long getAddr() {
        return addr;
    }

    @Override
    public int getStructSize() {
        return getStructSize(nAllocatedExtents);
    }

    @Override
    public int getNumberAllocatedExtents() {
        return nAllocatedExtents;
    }

    @Override
    public native void close() throws Exception;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RetrievalPointersBufferNative.class.getSimpleName()).append('(');
        sb.append("pointer=").append(Long.toHexString(addr)).append(", ");
        sb.append("size=").append(nAllocatedExtents);
        return sb.append(')').toString();
    }

    public static class ExtentNative implements Extent {
        @java.lang.annotation.Native
        private final long addr;

        public ExtentNative(long addr) {
            this.addr = addr;
        }

        @Override
        public native long getNextVcn();

        @Override
        public native long getLcn();
    }
}
