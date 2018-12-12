package me.nithanim.fragmentationstatistics.natives.windows;

import com.sun.jna.platform.win32.WinNT;
import java.io.IOException;
import java.nio.file.Path;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

public interface Winapi extends FileSystemUtil {
    WinNT.HANDLE createFile(Path p) throws IOException;

    void closeHandle(WinNT.HANDLE h);

    public int getLastError();

    /**
     * Fills the output buffer starting from given VCN in the input buffer.
     *
     * @param file
     * @param inputBuffer
     * @param outputBuffer
     * @return true if all data returned, if false call again with updated
     * nextVCN in inputBuffer
     */
    boolean fetchData(WinNT.HANDLE file, StartingVcnInputBuffer inputBuffer, RetrievalPointersBuffer outputBuffer);

    InternalFileSystemInformation getInternalFileSystemInformation(Path p);
}
