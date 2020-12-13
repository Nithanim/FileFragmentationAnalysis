package me.nithanim.fragmentationstatistics.natives.windows;

import java.io.IOException;
import java.nio.file.Path;
import jdk.incubator.foreign.MemoryAddress;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

public interface Winapi extends FileSystemUtil {
    MemoryAddress createFile(Path p) throws IOException;

    void closeHandle(MemoryAddress h) throws IOException;

    /**
     * Fills the output buffer starting from given VCN in the input buffer.
     *
     * @param fileHandle
     * @param inputBuffer
     * @param outputBuffer
     * @return true if all data returned, if false call again with updated
     * nextVCN in inputBuffer
     */
    boolean fetchData(MemoryAddress fileHandle, StartingVcnInputBuffer inputBuffer, RetrievalPointersBuffer outputBuffer) throws IOException;
    
    StartingVcnInputBuffer allocateStartingVcnInputBuffer();
    
    RetrievalPointersBuffer allocateRetrievalPointersBuffer(int nElements);
}
