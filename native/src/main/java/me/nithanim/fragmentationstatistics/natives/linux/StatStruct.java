package me.nithanim.fragmentationstatistics.natives.linux;

import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemorySegment;

/**
 * The fstat struct https://linux.die.net/man/2/fstat
 */
public class StatStruct implements Struct {
    private static final GroupLayout LAYOUT = MemoryLayout.ofStruct(//
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_dev"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_ino"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_nlink"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("st_mode"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("st_uid"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("st_gid"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("__pad0"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_rdev"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_size"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_blksize"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_blocks"),

            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_atime"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_atimensec"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_mtime"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_mtimensec"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_ctime"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("st_ctimensec"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("__glibc_reserved1"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("__glibc_reserved2"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("__glibc_reserved3")
    ).withName("fstat");

    private static final VarHandle accessorDev = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_dev")
    );
    private static final VarHandle accessorIno = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_ino")
    );
    private static final VarHandle accessorNlink = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_nlink")
    );
    private static final VarHandle accessorMode = LAYOUT.varHandle(int.class,
            MemoryLayout.PathElement.groupElement("st_mode")
    );
    private static final VarHandle accessorUid = LAYOUT.varHandle(int.class,
            MemoryLayout.PathElement.groupElement("st_uid")
    );
    private static final VarHandle accessorGid = LAYOUT.varHandle(int.class,
            MemoryLayout.PathElement.groupElement("st_gid")
    );
    private static final VarHandle accessorRdev = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_rdev")
    );
    private static final VarHandle accessorSize = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_size")
    );
    private static final VarHandle accessorBlksize = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_blksize")
    );
    private static final VarHandle accessorBlocks = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_blocks")
    );
    private static final VarHandle accessorAtime = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_atime")
    );
    private static final VarHandle accessorMtime = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_mtime")
    );
    private static final VarHandle accessorCtime = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("st_ctime")
    );

    private final MemorySegment memorySegment;

    public StatStruct() {
        this.memorySegment = MemorySegment.allocateNative(LAYOUT);
    }

    /**
     * ID of device containing file.
     */
    public long getDev() {
        return (long) accessorDev.get(memorySegment);
    }

    /**
     * inode number
     */
    public long getInodeNumber() {
        return (long) accessorIno.get(memorySegment);
    }

    /**
     * protection
     */
    public int getMode() {
        return (int) accessorMode.get(memorySegment);
    }

    /**
     * number of hard links
     */
    public long getNumberHardlinks() {
        return (long) accessorNlink.get(memorySegment);
    }

    /**
     * user ID of owner
     */
    public int getUid() {
        return (int) accessorUid.get(memorySegment);
    }

    /**
     * group ID of owner
     */
    public int getGid() {
        return (int) accessorGid.get(memorySegment);
    }

    /**
     * device ID (if device)
     */
    public long getRdev() {
        return (long) accessorRdev.get(memorySegment);
    }

    /**
     * total size, in bytes
     */
    public long getSize() {
        return (long) accessorSize.get(memorySegment);
    }

    /**
     * blocksize for file system I/O
     */
    public long getBlksize() {
        return (long) accessorBlksize.get(memorySegment);
    }

    /**
     * number of 512B blocks allocated
     */
    public long getBlocks() {
        return (long) accessorBlocks.get(memorySegment);
    }

    /**
     * time of last access
     */
    public OffsetDateTime getATime() {
        long raw = (long) accessorAtime.get(memorySegment);
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(raw), ZoneId.systemDefault());
    }

    /**
     * time of last modification
     */
    public OffsetDateTime getMTime() {
        long raw = (long) accessorMtime.get(memorySegment);
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(raw), ZoneId.systemDefault());
    }

    /**
     * time of last status change
     */
    public OffsetDateTime getCTime() {
        long raw = (long) accessorCtime.get(memorySegment);
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(raw), ZoneId.systemDefault());
    }

    @Override
    public void close() {
        memorySegment.close();
    }

    @Override
    public MemorySegment getMemorySegment() {
        return this.memorySegment;
    }
}
