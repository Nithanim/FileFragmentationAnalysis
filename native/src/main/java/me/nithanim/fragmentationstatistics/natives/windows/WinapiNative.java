package me.nithanim.fragmentationstatistics.natives.windows;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT;
import java.io.IOException;
import java.nio.file.Path;
import me.nithanim.fragmentationstatistics.natives.NativeCallException;
import me.nithanim.fragmentationstatistics.natives.NativeLoader;

public class WinapiNative implements Winapi {
    private static final int ERROR_FILE_NOT_FOUND = 2;

    static {
        NativeLoader.loadLibrary();
    }

    private static native long getInvalidHandleValue();

    @Override
    public WinNT.HANDLE createFile(Path p) throws IOException {
        try {
            long h = createFile(p.toString());
            return new WinNT.HANDLE(new Pointer(h));
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
    public void closeHandle(WinNT.HANDLE h) {
        Kernel32.INSTANCE.CloseHandle(h);
    }

    public native boolean closeHandle(long h);

    @Override
    public boolean fetchData(WinNT.HANDLE file, StartingVcnInputBuffer inputBuffer, RetrievalPointersBuffer outputBuffer) {
        int r = fillRetrievalPointers(Pointer.nativeValue(file.getPointer()), inputBuffer.getAddr(), outputBuffer.getAddr(), outputBuffer.getNumberAllocatedExtents());
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
    public native int getLastError();

    @Override
    public FileSystemInformation getFileSystemInformation(Path p) {
        InternalFileSystemInformation ifsi = getInternalFileSystemInformation(p);
        return new FileSystemInformation(ifsi.getFileSystemName(), null);
    }

    @Override
    public InternalFileSystemInformation getInternalFileSystemInformation(Path p) {
        return getInternalFileSystemInformation(p.toString());
    }

    public native InternalFileSystemInformation getInternalFileSystemInformation(String p);

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.WINDOWS;
    }
}
