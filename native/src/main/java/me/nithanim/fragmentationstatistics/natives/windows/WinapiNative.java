package me.nithanim.fragmentationstatistics.natives.windows;

import com.sun.jna.platform.win32.WinError;
import java.io.IOException;
import java.nio.file.Path;
import me.nithanim.fragmentationstatistics.natives.NativeCallException;
import me.nithanim.fragmentationstatistics.natives.NativeLoader;

public class WinapiNative implements Winapi {
    private static final int ERROR_FILE_NOT_FOUND = 2;

    static {
        NativeLoader.loadLibrary();
    }

    @Override
    public long createFile(Path p) throws IOException {
        try {
            return createFile(p.toString());
        } catch (NativeCallException ex) {
            if (ex.getErrorCode() == ERROR_FILE_NOT_FOUND) {
                throw new IOException("Unable to open file " + p, ex);
            } else {
                throw ex;
            }
        }
    }

    public native long createFile(String p);

    @Override
    public native void closeHandle(long h);

    @Override
    public boolean fetchData(long fileHandle, StartingVcnInputBuffer inputBuffer, RetrievalPointersBuffer outputBuffer) {
        int r = fillRetrievalPointers(fileHandle, inputBuffer.getAddr(), outputBuffer.getAddr(), outputBuffer.getNumberAllocatedExtents());
        if (r != 0) {
            if (r == WinError.ERROR_HANDLE_EOF) {
                return true;
            } else if (r == WinError.ERROR_MORE_DATA) {
                return false;
            } else {
                throw new WinapiCallException("DeviceIoControl with FSCTL_GET_RETRIEVAL_POINTERS");
            }
        } else {
            return true;
        }
    }

    /**
     * Returns GetLastError of DeviceIoControl
     *
     * @param handle
     * @param inputBufferAddr
     * @param outputBufferAddr
     * @param nAllocatedExtents cant be queried (easily) and so given as
     * parameter
     * @return
     */
    private native int fillRetrievalPointers(long handle, long inputBufferAddr, long outputBufferAddr, int nAllocatedExtents);

    @Override
    public FileSystemInformation getFileSystemInformation(Path p) {
        return getFileSystemInformation(p.toString());
    }

    private native FileSystemInformation getFileSystemInformation(String p);

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.WINDOWS;
    }

    @Override
    public StartingVcnInputBuffer allocateStartingVcnInputBuffer() {
        return StartingVcnInputBufferNative.allocate();
    }

    @Override
    public RetrievalPointersBuffer allocateRetrievalPointersBuffer(int nElements) {
        return RetrievalPointersBufferNative.allocate(nElements);
    }
}
