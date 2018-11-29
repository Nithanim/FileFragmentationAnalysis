package me.nithanim.fragmentationstatistics.natives.linux;

import java.io.IOException;
import java.nio.file.Path;

public interface LinuxApi {
    int openFileForReading(Path path) throws IOException;
    
    void closeFile(int fd); 
    
    int getBlocksize(int fd);

    long getFilesystemType(Path path);
    
    void fstat(int fd, StatStruct stat);
    
    void fillFiemap(int fd, FiemapStruct fs);
    
    FiemapStruct allocateFiemapStruct(int maxExtents);
    
    int fibmap(int fd, int idx);
}
