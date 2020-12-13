package me.nithanim.fragmentationstatistics.natives.windows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import jdk.incubator.foreign.CLinker;
import static jdk.incubator.foreign.CLinker.C_CHAR;
import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_POINTER;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.LibraryLookup;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;

public class WindowsPanamaFunctions {

    private static final MethodHandle GetLastError;
    private static final MethodHandle FormatMessage;
    private static final MethodHandle CreateFileW;
    private static final MethodHandle CloseHandle;
    private static final MethodHandle DeviceIoControl;
    private static final MethodHandle GetDiskFreeSpaceW;
    private static final MethodHandle GetVolumePathNameW;
    private static final MethodHandle GetVolumeInformationW;

    private static final List<LibraryLookup> lookups;


    static {
        ArrayList<LibraryLookup> ls = new ArrayList<>();
        ls.add(LibraryLookup.ofDefault());
        lookups = Collections.unmodifiableList(ls);
    }

    private static Optional<LibraryLookup.Symbol> lookup(String name) {
        return lookups.stream().map(l -> l.lookup(name)).filter(Optional::isPresent).map(Optional::get).findFirst();
    }


    static {
        GetLastError = CLinker.getInstance()
                .downcallHandle(lookup("GetLastError").get(),
                        MethodType.methodType(int.class),
                        FunctionDescriptor.of(C_INT)
                );

        FormatMessage = CLinker.getInstance()
                .downcallHandle(lookup("FormatMessage").get(), MethodType.methodType(int.class,
                        int.class,
                        MemoryAddress.class,
                        int.class,
                        int.class,
                        MemoryAddress.class,
                        int.class,
                        MemoryAddress.class
                ), FunctionDescriptor.of(C_INT, C_INT, C_POINTER, C_INT, C_INT, C_POINTER, C_INT, C_POINTER));

        CreateFileW = CLinker.getInstance().downcallHandle(lookup("CreateFileW").get(), MethodType.methodType(
                MemoryAddress.class,
                MemoryAddress.class,
                int.class,
                int.class,
                MemoryAddress.class,
                int.class,
                int.class,
                MemoryAddress.class
        ), FunctionDescriptor.of(C_POINTER, C_INT, C_INT, C_POINTER, C_INT, C_INT, C_POINTER));

        CloseHandle = CLinker.getInstance().downcallHandle(lookup("CloseHandle").get(),
                MethodType.methodType(boolean.class, MemoryAddress.class),
                FunctionDescriptor.of(C_CHAR, C_POINTER)
        );

        DeviceIoControl = CLinker.getInstance().downcallHandle(lookup("DeviceIoControl").get(), MethodType.methodType(
                boolean.class,
                MemoryAddress.class,
                int.class,
                MemoryAddress.class,
                int.class,
                MemoryAddress.class,
                int.class,
                MemoryAddress.class,
                MemoryAddress.class
        ), FunctionDescriptor.of(C_CHAR, C_POINTER, C_INT, C_POINTER, C_INT, C_POINTER, C_INT, C_POINTER, C_POINTER));

        GetDiskFreeSpaceW = CLinker.getInstance().downcallHandle(lookup("DeviceIoControl").get(), MethodType.methodType(
                boolean.class,
                MemoryAddress.class,
                MemoryAddress.class,
                MemoryAddress.class,
                MemoryAddress.class,
                MemoryAddress.class
        ), FunctionDescriptor.of(C_CHAR, C_POINTER, C_POINTER, C_POINTER, C_POINTER, C_POINTER));

        GetVolumePathNameW = CLinker.getInstance()
                .downcallHandle(
                        lookup("GetVolumePathNameW").get(),
                        MethodType.methodType(boolean.class, MemoryAddress.class, MemoryAddress.class, int.class),
                        FunctionDescriptor.of(C_CHAR, C_POINTER, C_POINTER, C_INT)
                );

        GetVolumeInformationW = CLinker.getInstance()
                .downcallHandle(
                        lookup("GetVolumeInformationW").get(),
                        MethodType.methodType(boolean.class,
                                MemoryAddress.class,
                                MemoryAddress.class,
                                int.class,
                                MemoryAddress.class,
                                MemoryAddress.class,
                                MemoryAddress.class,
                                MemoryAddress.class,
                                int.class
                        ),
                        FunctionDescriptor.of(C_CHAR,
                                C_POINTER,
                                C_POINTER,
                                C_INT,
                                C_POINTER,
                                C_POINTER,
                                C_POINTER,
                                C_POINTER,
                                C_INT
                        )
                );
    }

    public static MemoryAddress createFile(MemorySegment lpFileName,
            int dwDesiredAccess,
            int dwShareMode,
            int dwCreationDisposition,
            int dwFlagsAndAttributes) throws Throwable {
        return (MemoryAddress) CreateFileW.invokeExact(lpFileName,
                dwDesiredAccess,
                dwShareMode,
                0,
                dwCreationDisposition,
                dwFlagsAndAttributes,
                0
        );
    }

    public static int GetLastError() throws Throwable {
        return (int) GetLastError.invokeExact();
    }

    public static int FormatMessage(int dwFlags,
            MemoryAddress lpSource,
            int dwMessageId,
            int dwLanguageId,
            MemorySegment lpBuffer) throws Throwable {
        return (int) FormatMessage.invokeExact(dwFlags,
                lpSource,
                dwMessageId,
                dwLanguageId,
                lpBuffer.address(),
                lpBuffer.byteSize(),
                MemoryAddress.NULL
        );
    }

    public static boolean CloseHandle(MemoryAddress handle) throws Throwable {
        return (boolean) CloseHandle.invokeExact(handle);
    }

    public static boolean FSCTL_GET_RETRIEVAL_POINTERS(MemoryAddress handle,
            MemorySegment lpInBuffer,
            MemorySegment lpOutBuffer,
            MemorySegment lpBytesReturned) throws Throwable {
        return (boolean) DeviceIoControl.invokeExact(handle,
                WindowsConstants.FSCTL_GET_RETRIEVAL_POINTERS,
                lpInBuffer.address(),
                lpInBuffer.byteSize(),
                lpOutBuffer.address(),
                lpOutBuffer.byteSize(),
                lpBytesReturned.address(),
                MemoryAddress.NULL
        );
    }


    public static boolean GetVolumePathNameW(MemorySegment volumePathName, MemorySegment filesystemName) throws Throwable {
        return (boolean) GetVolumePathNameW.invokeExact(volumePathName.address(),
                filesystemName.address(),
                filesystemName.byteSize()
        );
    }

    public static boolean GetDiskFreeSpaceW(MemoryAddress lpRootPathName,
            MemorySegment lpSectorsPerCluster,
            MemorySegment lpBytesPerSector,
            MemorySegment lpNumberOfFreeClusters,
            MemorySegment lpTotalNumberOfClusters) throws Throwable {
        return (boolean) GetDiskFreeSpaceW.invokeExact(lpRootPathName,
                lpSectorsPerCluster.address(),
                lpBytesPerSector.address(),
                lpNumberOfFreeClusters.address(),
                lpTotalNumberOfClusters.address()
        );
    }

    public static boolean GetVolumeInformationW(
            MemoryAddress lpRootPathName,
            MemorySegment  lpVolumeNameBuffer,
            MemoryAddress lpVolumeSerialNumber,
            MemoryAddress lpMaximumComponentLength,
            MemoryAddress lpFileSystemFlags,
            MemorySegment lpFileSystemNameBuffer
    ) throws Throwable {
return (boolean) GetVolumeInformationW.invokeExact(
        lpRootPathName.address(),
        lpVolumeNameBuffer.address(),
        lpVolumeNameBuffer.byteSize(),
        lpVolumeSerialNumber.address(),
        lpMaximumComponentLength.address(),
        lpFileSystemFlags.address(),
        lpFileSystemNameBuffer.address(),
        lpFileSystemNameBuffer.byteSize()
        );
    }

    public static String fromWString(MemorySegment str) {
        int len = strlen(str);
        byte[] bytes = new byte[len];
        MemorySegment.ofArray(bytes).copyFrom(str.asSlice(0, len));
        return new String(bytes, StandardCharsets.UTF_16);
    }

    private static int strlen(MemorySegment segment) {
        for (int offset = 0; ; offset++) {
            byte c = MemoryAccess.getByteAtOffset(segment, offset);
            if (c == 0) {
                return offset;
            }
        }
    }

    public static MemorySegment toWString(String str) {
        return toWString(str.getBytes(StandardCharsets.UTF_16));
    }

    private static MemorySegment toWString(byte[] bytes) {
        MemorySegment segment = MemorySegment.allocateNative(bytes.length + 2, 2);
        copy(segment, bytes);
        return segment;
    }

    private static void copy(MemorySegment addr, byte[] bytes) {
        var heapSegment = MemorySegment.ofArray(bytes);
        addr.copyFrom(heapSegment);
        MemoryAccess.setShortAtOffset(addr, bytes.length - 1, (short) 0);
    }

    public static int MAKELANGID(int p, int s) {
        return (((s) << 10) | p);
    }
}

