package me.nithanim.fragmentationstatistics.natives.linux;

import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.MemoryLayout;
import static jdk.incubator.foreign.MemoryLayout.PathElement.groupElement;
import jdk.incubator.foreign.MemorySegment;

public class StatVfsStructPanama implements StatVfsStruct {
    private static final GroupLayout LAYOUT = MemoryLayout.ofStruct(//
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_bsize"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_frsize"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_blocks"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_bfree"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_bavail"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_files"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_ffree"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_favail"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_fsid"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_flag"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("f_namemax"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("__f_spare1"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("__f_spare2"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("__f_spare3"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("__f_spare4"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("__f_spare5"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("__f_spare6")
    ).withName("statvfs");

    private static final VarHandle accessorBsize = LAYOUT.varHandle(long.class, groupElement("f_bsize"));
    private static final VarHandle accessorFrsize = LAYOUT.varHandle(long.class, groupElement("f_frsize"));
    private static final VarHandle accessorBlocks = LAYOUT.varHandle(long.class, groupElement("f_blocks"));
    private static final VarHandle accessorBfree = LAYOUT.varHandle(long.class, groupElement("f_bfree"));
    private static final VarHandle accessorBavail = LAYOUT.varHandle(long.class, groupElement("f_bavail"));
    private static final VarHandle accessorFfree = LAYOUT.varHandle(long.class, groupElement("f_ffree"));
    private static final VarHandle accessorFiles = LAYOUT.varHandle(long.class, groupElement("f_files"));
    private static final VarHandle accessorFavail = LAYOUT.varHandle(long.class, groupElement("f_favail"));
    private static final VarHandle accessorFsid = LAYOUT.varHandle(long.class, groupElement("f_fsid"));
    private static final VarHandle accessorFlag = LAYOUT.varHandle(long.class, groupElement("f_flag"));
    private static final VarHandle accessorNamemax = LAYOUT.varHandle(long.class, groupElement("f_namemax"));

    private final MemorySegment memorySegment;

    public StatVfsStructPanama() {
        this.memorySegment = MemorySegment.allocateNative(LAYOUT);
    }

    @Override
    public long getPreferredBlocksize() {
        return (long) accessorBsize.get(memorySegment);
    }

    @Override
    public long getMinimumBlockSize() {
        return (long) accessorFrsize.get(memorySegment);
    }

    @Override
    public long getTotalNumberOfBlocks() {
        return (long) accessorBlocks.get(memorySegment);
    }

    @Override
    public long getNumberOfFreeBlocks() {
        return (long) accessorBfree.get(memorySegment);
    }

    @Override
    public long getNumberOfFreeBlocksUnprivileged() {
        return (long) accessorBavail.get(memorySegment);
    }

    @Override
    public long getNumberOfInodes() {
        return (long) accessorFiles.get(memorySegment);
    }

    @Override
    public long getNumberOfFreeInodes() {
        return (long) accessorFfree.get(memorySegment);
    }

    @Override
    public long getNumberOfFreeInodesUnprivileged() {
        return (long) accessorFavail.get(memorySegment);
    }

    @Override
    public long getFileSystemId() {
        return (long) accessorFsid.get(memorySegment);
    }

    @Override
    public long getMountFlags() {
        return (long) accessorFlag.get(memorySegment);
    }

    @Override
    public long getMaxFileNameLength() {
        return (long) accessorNamemax.get(memorySegment);
    }

    @Override
    public void close() {
        memorySegment.close();
    }

    @Override
    public MemorySegment getMemorySegment() {
        return memorySegment;
    }
}
