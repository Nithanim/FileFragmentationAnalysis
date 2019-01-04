package me.nithanim.fragmentationstatistics.natives.windows;

import java.io.IOException;
import java.nio.file.Path;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

public interface Winapi extends FileSystemUtil {
    long createFile(Path p) throws IOException;

    void closeHandle(long h);

    /**
     * Fills the output buffer starting from given VCN in the input buffer.
     *
     * @param fileHandle
     * @param inputBuffer
     * @param outputBuffer
     * @return true if all data returned, if false call again with updated
     * nextVCN in inputBuffer
     */
    boolean fetchData(long fileHandle, StartingVcnInputBuffer inputBuffer, RetrievalPointersBuffer outputBuffer);
}
