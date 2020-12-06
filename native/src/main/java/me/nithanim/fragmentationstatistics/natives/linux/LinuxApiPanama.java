package me.nithanim.fragmentationstatistics.natives.linux;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.nio.ByteOrder;
import java.nio.file.Path;
import jdk.incubator.foreign.CLinker;
import static jdk.incubator.foreign.CLinker.C_POINTER;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.LibraryLookup;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemorySegment;
import lombok.SneakyThrows;

public class LinuxApiPanama implements LinuxApi {
    private static final int O_RDONLY = 0;
    private static final int EPERM = 1;

    @Override
    public int openFileForReading(Path path) throws IOException {
        try (MemorySegment str = CLinker.toCString(path.toString())) {
            int r = LinuxPanamaFunctions.open(str.address(), O_RDONLY);
            if (r < 0) {
                int errno = getErrno();
                throw new IOException("Unable to open file for reading: " + getStrerror(errno) + " (" + errno + ")");
            } else {
                return r;
            }
        } catch (IOException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @Override
    public void closeFile(int fd) throws IOException {
        try {
            int r = (int) LinuxPanamaFunctions.close(fd);
            if (r == -1) {
                int e = getErrno();
                throw new IOException("Unable to close file: " + getStrerror(e) + " (" + e + ")");
            }
        } catch (Error e) {
            throw e;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @Override
    public int getBlocksize(int fd) throws IOException {
        try (MemorySegment blocksizeStorage = MemorySegment.allocateNative(CLinker.C_INT.byteSize())) {
            int r = (int) LinuxPanamaFunctions.blocksize.invokeExact(fd,
                    PanamaConstants.FIGETBSZ,
                    blocksizeStorage.address()
            );
            if (r == -1) {
                int e = getErrno();
                throw new IOException("Unable to get blocksize: " + getStrerror(e) + " (" + e + ")");
            } else {
                return MemoryAccess.getInt(blocksizeStorage);
            }
        } catch (IOException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @Override
    public long getFilesystemType(Path path) throws IOException {
        GroupLayout structDef = MemoryLayout.ofStruct(//
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_type"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_bsize"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_blocks"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_bfree"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_bavail"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_files"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_ffree"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_fsid"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_namelen"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_frsize"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_flags"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_spare1"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_spare2"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_spare3"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_spare4")
        ).withName("statfs");

        try (MemorySegment str = CLinker.toCString(path.toString()); MemorySegment structAlloc = MemorySegment.allocateNative(
                structDef)) {
            int r = (int) LinuxPanamaFunctions.statfs.invokeExact(str.address(), structAlloc.address());
            if (r == -1) {
                int e = getErrno();
                throw new IOException("Unable to call statfs: " + getStrerror(e) + " (" + e + ")");
            } else {
                return MemoryAccess.getLong(structAlloc);
            }
        } catch (IOException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @Override
    public void fstat(int fd, StatStruct stat) throws IOException {
        try {
            int r = LinuxPanamaFunctions.fstat(fd, ((StatStruct) stat).getMemorySegment().address());
            if (r == -1) {
                int e = getErrno();
                throw new IOException("Unable to call stat: " + getStrerror(e) + " (" + e + ")");
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
    public void stat(Path file, StatStruct stat) throws IOException {
        try (MemorySegment str = CLinker.toCString(file.toString())) {
            int r = LinuxPanamaFunctions.stat(str.address(), ((StatStruct) stat).getMemorySegment().address());
            if (r == -1) {
                int e = getErrno();
                throw new IOException("Unable to call stat: " + getStrerror(e) + " (" + e + ")");
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
    public void statvfs(Path file, StatVfsStruct stat) throws IOException {
        try (MemorySegment str = CLinker.toCString(file.toString())) {
            int r = LinuxPanamaFunctions.statvfs(str.address(), stat);
            if (r == -1) {
                int e = getErrno();
                throw new IOException("Unable to call stat: " + getStrerror(e) + " (" + e + ")");
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
    public void fillFiemap(int fd, FiemapStruct fs) throws IOException {
        try {
            int r = LinuxPanamaFunctions.fiemap(fd, fs.getMemorySegment().address());
            if (r == -1) {
                int e = getErrno();
                throw new IOException("Unable to call stat: " + getStrerror(e) + " (" + e + ")");
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
    public FiemapStruct allocateFiemapStruct(int maxExtents) {
        return new FiemapStructPanama(maxExtents);
    }

    @Override
    public int fibmap(int fd, int idx) throws IOException {
        try (MemorySegment block = MemorySegment.allocateNative(CLinker.C_INT.byteSize())) {
            MemoryAccess.setInt(block, idx);
            int r = LinuxPanamaFunctions.fibmap(fd, block.address());
            if (r == -1) {
                int errno = getErrno();
                if (errno == EPERM) {
                    throw new RootRequiredException();
                } else {
                    throw new IOException("Unable to call fibmap: " + getStrerror(errno) + " (" + errno + ")");
                }
            } else {
                return MemoryAccess.getInt(block);
            }
        } catch (IOException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @Override
    @SneakyThrows
    public FileSystemInformation getFileSystemInformation(Path p) {
        try (MemorySegment str = CLinker.toCString(p.toString()); StatVfsStruct ss = allocateStatVfsStruct()) {
            int r = LinuxPanamaFunctions.statvfs(str.address(), ss);
            if (r == -1) {
                int errno = getErrno();
                throw new IOException("Unable to call statvfs: " + getStrerror(errno) + " (" + errno + ")");
            }

            long magic = getFilesystemType(p);
            long blockSize = ss.getMinimumBlockSize();
            long freeSize = ss.getNumberOfFreeBlocks() * blockSize;
            long totalSize = ss.getTotalNumberOfBlocks() * blockSize;

            //TODO FS whose support is implemented via FUSE will have type "fuseblk".
            //Maybe get underlying blkdev and look into code of "lsblk -no name,fstype" (which returns e.g. ntfs) for possible fix.
            LinuxFileSystemType t = LinuxFileSystemType.getFileSystemType(magic);
            return new FileSystemInformation(t == null ? null : t.getName(), magic, totalSize, freeSize, blockSize);
        }
    }

    @Override
    public StatStruct allocateStatStruct() {
        return new StatStruct();
    }

    @Override
    public StatVfsStruct allocateStatVfsStruct() {
        return new StatVfsStructPanama();
    }

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.LINUX;
    }

    private int getErrno() {
        MemoryAddress location = getErrnoLocation();
        return MemoryAccess.getIntAtOffset(MemorySegment.ofNativeRestricted(), location.toRawLongValue());
    }

    @SneakyThrows
    private String getStrerror(int errno) {
        try (MemorySegment buf = MemorySegment.allocateNative(100)) {
            MemoryAddress r = (MemoryAddress) LinuxPanamaFunctions.strerror_r.invokeExact(errno,
                    buf.address(),
                    (int) buf.byteSize()
            );
            if (r.toRawLongValue() != 0) {
                return CLinker.toJavaString(r.asSegmentRestricted(buf.byteSize()));
            } else {
                throw new IllegalStateException("Unable to call strerror_r!");
            }
        }
    }

    @SneakyThrows
    private MemoryAddress getErrnoLocation() {
        LibraryLookup libc = LibraryLookup.ofDefault();

        MethodHandle open = CLinker.getInstance().downcallHandle(libc.lookup("__errno_location").get(),
                MethodType.methodType(MemoryAddress.class),
                FunctionDescriptor.of(C_POINTER)
        );
        return (MemoryAddress) open.invokeExact();
    }

}
