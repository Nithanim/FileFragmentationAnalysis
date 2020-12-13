package me.nithanim.fragmentationstatistics.natives.windows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemorySegment;

public class WinapiPanama implements Winapi {
    @Override
    public MemoryAddress createFile(Path path) throws IOException {
        try (MemorySegment str = WindowsPanamaFunctions.toWString(path.toString())) {
            var handle = WindowsPanamaFunctions.createFile(
                    str,
                    0,
                    WindowsConstants.FILE_SHARE_READ,
                    WindowsConstants.OPEN_EXISTING,
                    WindowsConstants.FILE_ATTRIBUTE_NORMAL
            );
            int error = WindowsPanamaFunctions.GetLastError();

            if (error != 0) {
                if (error == WindowsConstants.ERROR_FILE_NOT_FOUND) {
                    throw new FileNotFoundException(path.toString());
                } else {
                    throw new IOException("Unable to open " + path + ": " + getErrorString(error) + " (" + error + ")");
                }
            } else {
                return handle;
            }
        } catch (IOException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    private String getErrorString(int error) throws Throwable {
        try (MemorySegment buffer = MemorySegment.allocateNative(100, 2)) {
            int r = WindowsPanamaFunctions.FormatMessage(
                    WindowsConstants.FORMAT_MESSAGE_FROM_SYSTEM | WindowsConstants.FORMAT_MESSAGE_IGNORE_INSERTS,
                    MemoryAddress.NULL,
                    error,
                    WindowsPanamaFunctions.MAKELANGID(WindowsConstants.LANG_NEUTRAL, WindowsConstants.SUBLANG_DEFAULT),
                    buffer
            );
            if (r > 0) {
                return WindowsPanamaFunctions.fromWString(buffer);
            } else {
                throw new IllegalStateException("Unable to get error string!");
            }
        }
    }

    @Override
    public void closeHandle(MemoryAddress handle) throws IOException {
        try {
            boolean r = WindowsPanamaFunctions.CloseHandle(handle);
            if (r) {
                int error = WindowsPanamaFunctions.GetLastError();
                if (error != 0) {
                    throw new IOException("Unable close handle: " + getErrorString(error) + " (" + error + ")");
                }
            }
        } catch (Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @Override
    public boolean fetchData(MemoryAddress fileHandle,
            StartingVcnInputBuffer inputBuffer,
            RetrievalPointersBuffer outputBuffer) throws IOException {
        try (MemorySegment returnedBytes = MemorySegment.allocateNative(CLinker.C_POINTER)) {
            boolean r = WindowsPanamaFunctions.FSCTL_GET_RETRIEVAL_POINTERS(
                    fileHandle,
                    ((StartingVcnInputBufferPanama) inputBuffer).getMemorySegment(),
                    ((RetrievalPointersBufferPanama) outputBuffer).getMemorySegment(),
                    returnedBytes
            );
            if (r) {
                int error = WindowsPanamaFunctions.GetLastError();
                if (error == WindowsConstants.ERROR_HANDLE_EOF) {
                    return true;
                } else if (error == WindowsConstants.ERROR_MORE_DATA) {
                    return false;
                } else {
                    throw new IOException("Unable fetch FSCTL_GET_RETRIEVAL_POINTERS: " + getErrorString(error) + " (" + error + ")");
                }
            } else {
                int error = WindowsPanamaFunctions.GetLastError();
                throw new IOException("Unable fetch FSCTL_GET_RETRIEVAL_POINTERS: " + getErrorString(error) + " (" + error + ")");
            }
        } catch (Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @Override
    public FileSystemInformation getFileSystemInformation(Path p) throws IOException {
        try (MemorySegment volumePathName = MemorySegment.allocateNative(30); MemorySegment filesystemName = MemorySegment
                .allocateNative(30)) {

            if (!getVolumePathName(p, volumePathName)) {
                int error = WindowsPanamaFunctions.GetLastError();
                throw new IOException("Unable call GetVolumePathNameW: " + getErrorString(error) + " (" + error + ")");
            }

            if (!WindowsPanamaFunctions.GetVolumeInformationW(
                    volumePathName.address(),
                    MemoryAddress.NULL.asSegmentRestricted(0),
                    MemoryAddress.NULL,
                    MemoryAddress.NULL,
                    MemoryAddress.NULL,
                    filesystemName
            )) {
                int error = WindowsPanamaFunctions.GetLastError();
                throw new IOException("Unable call GetVolumeInformation: " + getErrorString(error) + " (" + error + ")");
            }

            int totalNumberOfClusters;
            int numberOfFreeClusters;
            int sectorsPerCluster;
            int bytesPerSector;
            try (MemorySegment ms = MemorySegment.allocateNative(MemoryLayout.ofSequence(4, CLinker.C_INT))) {
                MemorySegment msSectorsPerCluster = ms.asSlice(2 * CLinker.C_INT.byteSize(), CLinker.C_INT.byteSize());
                MemorySegment msBytesPerCluster = ms.asSlice(3 * CLinker.C_INT.byteSize(), CLinker.C_INT.byteSize());
                MemorySegment msNumberOfFreeClusters = ms.asSlice(CLinker.C_INT.byteSize(), CLinker.C_INT.byteSize());
                MemorySegment msTotalNumberOfClusters = ms.asSlice(0, CLinker.C_INT.byteSize());
                boolean r = WindowsPanamaFunctions.GetDiskFreeSpaceW(
                        volumePathName.address(),
                        msSectorsPerCluster,
                        msBytesPerCluster,
                        msNumberOfFreeClusters,
                        msTotalNumberOfClusters
                );
                if (r) {
                    int error = WindowsPanamaFunctions.GetLastError();
                    throw new IOException("Unable call GetDiskFreeSpaceW: " + getErrorString(error) + " (" + error + ")");
                } else {
                    totalNumberOfClusters = MemoryAccess.getInt(msTotalNumberOfClusters);
                    numberOfFreeClusters = MemoryAccess.getInt(msNumberOfFreeClusters);
                    sectorsPerCluster = MemoryAccess.getInt(msSectorsPerCluster);
                    bytesPerSector = MemoryAccess.getInt(msBytesPerCluster);
                }
            }


            String name = WindowsPanamaFunctions.fromWString(filesystemName);
            long fsBlockSize = (long) sectorsPerCluster * bytesPerSector;
            long fsTotalSize = totalNumberOfClusters * fsBlockSize;
            long fsFreeSize = numberOfFreeClusters * fsBlockSize;
            return new FileSystemInformation(name, 0, fsTotalSize, fsFreeSize, fsBlockSize);
        } catch (Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    private boolean getVolumePathName(Path p, MemorySegment volumePathName) throws Throwable {
        try (MemorySegment filePath = WindowsPanamaFunctions.toWString(p.toString())) {
            return WindowsPanamaFunctions.GetVolumePathNameW(filePath, volumePathName);
        }
    }

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.WINDOWS;
    }

    @Override
    public StartingVcnInputBuffer allocateStartingVcnInputBuffer() {
        return new StartingVcnInputBufferPanama();
    }

    @Override
    public RetrievalPointersBuffer allocateRetrievalPointersBuffer(int nElements) {
        return new RetrievalPointersBufferPanama(nElements);
    }
}
