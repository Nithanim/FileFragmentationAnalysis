package me.nithanim.fragmentationstatistics.natives.linux;

import java.io.IOException;
import java.nio.file.Path;
import me.nithanim.fragmentationstatistics.natives.NativeLoader;

public class LinuxApiNative implements LinuxApi {
    static {
        NativeLoader.loadLibrary();
    }

    @Override
    public int openFileForReading(Path path) throws IOException {
        try {
            return openFileForReading(path.toString());
        } catch (Exception ex) {
            throw new IOException("Unable to open file " + path, ex);
        }
    }

    public native int openFileForReading(String path);

    @Override
    public native void closeFile(int fd);

    @Override
    public native int getBlocksize(int fd);

    @Override
    public long getFilesystemType(Path path) {
        return getFilesystemType(path.toString());
    }

    private native long getFilesystemType(String path);

    @Override
    public void fstat(int fd, StatStruct stat) {
        fstat(fd, stat.getAddr());
    }

    private native void fstat(int fd, long statStructPointer);

    @Override
    public void fillFiemap(int fd, FiemapStruct fs) {
        fillFiemap(fd, ((FiemapStructNative) fs).getAddr());
    }

    private native void fillFiemap(int fd, long fs);

    @Override
    public FiemapStruct allocateFiemapStruct(int maxExtents) {
        return FiemapStructNative.allocate(maxExtents);
    }

    @Override
    public native int fibmap(int fd, int idx);
}