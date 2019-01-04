package me.nithanim.fragmentationstatistics.natives.linux;

import java.io.IOException;
import java.nio.file.Path;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

public interface LinuxApi extends FileSystemUtil {
    int openFileForReading(Path path) throws IOException;
    
    void closeFile(int fd); 
    
    int getBlocksize(int fd);

    long getFilesystemType(Path path);
    
    void fstat(int fd, StatStruct stat);
    
    void stat(Path file, StatStruct stat);
    
    void statvfs(Path file, StatVfsStruct stat);
    
    void fillFiemap(int fd, FiemapStruct fs);
    
    FiemapStruct allocateFiemapStruct(int maxExtents);
    
    StatVfsStruct allocateStatVfsStruct();
    
    int fibmap(int fd, int idx);
}
