package me.nithanim.fragmentationstatistics.natives.linux;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import jdk.incubator.foreign.CLinker;
import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_POINTER;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.LibraryLookup;
import jdk.incubator.foreign.MemoryAddress;
import static me.nithanim.fragmentationstatistics.natives.linux.PanamaConstants.FIBMAP;
import static me.nithanim.fragmentationstatistics.natives.linux.PanamaConstants.FS_IOC_FIEMAP;
import static me.nithanim.fragmentationstatistics.natives.linux.PanamaConstants._STAT_VER;

public class LinuxPanamaFunctions {

    private static final MethodHandle open;
    private static final MethodHandle close;
    public static final MethodHandle strerror_r;
    public static final MethodHandle blocksize;
    public static final MethodHandle statfs;
    private static final MethodHandle __xstat;
    private static final MethodHandle __fxstat;
    private static final MethodHandle fibmap;
    public static final MethodHandle statvfs;
    private static final MethodHandle fiemap;

    private static final List<LibraryLookup> lookups;

    static {
        ArrayList<LibraryLookup> ls = new ArrayList<>();
        ls.add(LibraryLookup.ofDefault());
        try {
            ls.add(LibraryLookup.ofPath(Paths.get("/lib/x86_64-linux-gnu/libc.so.6")));
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        lookups = Collections.unmodifiableList(ls);
    }

    private static Optional<LibraryLookup.Symbol> lookup(String name) {
        return lookups.stream().map(l -> l.lookup(name)).filter(Optional::isPresent).map(Optional::get).findFirst();
    }


    static {


        open = CLinker.getInstance().downcallHandle(
                lookup("open").get(),
                MethodType.methodType(int.class, MemoryAddress.class, int.class),
                FunctionDescriptor.of(C_INT, C_POINTER, C_INT)
        );

        close = CLinker.getInstance().downcallHandle(
                lookup("close").get(),
                MethodType.methodType(int.class, int.class),
                FunctionDescriptor.of(C_INT, C_INT)
        );

        strerror_r = CLinker.getInstance().downcallHandle(
                lookup("strerror_r").get(),
                MethodType.methodType(MemoryAddress.class, int.class, MemoryAddress.class, int.class),
                FunctionDescriptor.of(C_POINTER, C_INT, C_POINTER, C_INT)
        );

        blocksize = CLinker.getInstance().downcallHandle(
                lookup("ioctl").get(),
                MethodType.methodType(int.class, int.class, int.class, MemoryAddress.class),
                FunctionDescriptor.of(C_INT, C_INT, C_INT, C_POINTER)
        );

        statfs = CLinker.getInstance().downcallHandle(
                lookup("statfs").get(),
                MethodType.methodType(int.class, MemoryAddress.class, MemoryAddress.class),
                FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER)
        );

        __xstat = CLinker.getInstance().downcallHandle(
                lookup("__xstat").get(),
                MethodType.methodType(int.class, int.class, MemoryAddress.class, MemoryAddress.class),
                FunctionDescriptor.of(C_INT, C_INT, C_POINTER, C_POINTER)
        );

        __fxstat = CLinker.getInstance().downcallHandle(
                lookup("__fxstat").get(),
                MethodType.methodType(int.class, int.class, int.class, MemoryAddress.class),
                FunctionDescriptor.of(C_INT, C_INT, C_INT, C_POINTER)
        );

        fibmap = CLinker.getInstance().downcallHandle(
                lookup("ioctl").get(),
                MethodType.methodType(int.class, int.class, int.class, int.class),
                FunctionDescriptor.of(C_INT, C_INT, C_INT, C_INT)
        );

        statvfs = CLinker.getInstance().downcallHandle(
                lookup("statvfs").get(),
                MethodType.methodType(int.class, MemoryAddress.class, MemoryAddress.class),
                FunctionDescriptor.of(C_INT, C_POINTER, C_POINTER)
        );

        fiemap = CLinker.getInstance().downcallHandle(
                lookup("ioctl").get(),
                MethodType.methodType(int.class, int.class, int.class, MemoryAddress.class),
                FunctionDescriptor.of(C_INT, C_INT, C_INT, C_POINTER)
        );
    }

    public static int open(MemoryAddress file, int options) throws Throwable {
        return (int) open.invokeExact(file, options);
    }

    public static int close(int fd) throws Throwable {
        return (int) close.invokeExact(fd);
    }

    public static int stat(MemoryAddress file, MemoryAddress buf) throws Throwable {
        return (int) __xstat.invokeExact(_STAT_VER, file, buf);
    }

    public static int fstat(int fd, MemoryAddress buf) throws Throwable {
        return (int) __xstat.invokeExact(_STAT_VER, fd, buf);
    }

    public static int fibmap(int fd, MemoryAddress block) throws Throwable {
        return (int) close.invokeExact(fd, FIBMAP, block);
    }

    public static int statvfs(MemoryAddress file, StatVfsStruct ss) throws Throwable {
        return (int) statvfs.invokeExact(file, ss.getMemorySegment().address());
    }

    public static int fiemap(int fd, MemoryAddress fsAddr) throws Throwable {
        return (int) fiemap.invokeExact(fd, FS_IOC_FIEMAP, fsAddr);
    }
}

