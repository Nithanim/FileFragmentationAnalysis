package me.nithanim.fragmentationstatistics.natives.windows;

import java.io.IOException;
import java.nio.file.Path;
import me.nithanim.fragmentationstatistics.natives.NativeCallException;
import me.nithanim.fragmentationstatistics.natives.NativeLoader;

public class WinapiNative implements Winapi {
    private static final int ERROR_FILE_NOT_FOUND = 2;
    
    private static final int ERROR_HANDLE_EOF = 38;
    private static final int ERROR_MORE_DATA = 234;

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
            if (r == ERROR_HANDLE_EOF) {
                return true;
            } else if (r == ERROR_MORE_DATA) {
                return false;
            } else {
                throw new NativeCallException("DeviceIoControl with FSCTL_GET_RETRIEVAL_POINTERS", r, 0, null);
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
