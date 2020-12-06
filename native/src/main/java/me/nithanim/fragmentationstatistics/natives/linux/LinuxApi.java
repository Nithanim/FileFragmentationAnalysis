package me.nithanim.fragmentationstatistics.natives.linux;

import java.io.IOException;
import java.nio.file.Path;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

public interface LinuxApi extends FileSystemUtil {
    int openFileForReading(Path path) throws IOException;

    void closeFile(int fd) throws IOException;

    int getBlocksize(int fd) throws IOException;

    long getFilesystemType(Path path) throws IOException;

    void fstat(int fd, StatStruct stat) throws IOException;

    void stat(Path file, StatStruct stat) throws IOException;

    void statvfs(Path file, StatVfsStruct stat) throws IOException;

    void fillFiemap(int fd, FiemapStruct fs) throws IOException;

    FiemapStruct allocateFiemapStruct(int maxExtents);

    StatStruct allocateStatStruct();

    StatVfsStruct allocateStatVfsStruct();

    int fibmap(int fd, int idx) throws IOException;
}
