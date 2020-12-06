package me.nithanim.fragmentationstatistics.natives.linux;

import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemorySegment;

public class FiemapStructPanama implements FiemapStruct {
    public static final GroupLayout BASE_LAYOUT = MemoryLayout.ofStruct(//
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("fm_start"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("fm_length"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("fm_flags"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("fm_mapped_extents"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("fm_extent_count"),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("fm_reserved")
    ).withName("fiemap");
    private static final GroupLayout EXTENT_LAYOUT = FiemapExtentPanama.LAYOUT;

    private static final VarHandle accessorStart = BASE_LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("fm_start")
    );
    private static final VarHandle accessorLength = BASE_LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("fm_length")
    );
    private static final VarHandle accessorFlags = BASE_LAYOUT.varHandle(int.class,
            MemoryLayout.PathElement.groupElement("fm_flags")
    );
    private static final VarHandle accessorMappedExtents = BASE_LAYOUT.varHandle(int.class,
            MemoryLayout.PathElement.groupElement("fm_mapped_extents")
    );
    private static final VarHandle accessorExtentCount = BASE_LAYOUT.varHandle(int.class,
            MemoryLayout.PathElement.groupElement("fm_extent_count")
    );

    private final MemorySegment fullSegment;
    private final MemorySegment baseSegment;
    private final MemorySegment extentSegment;
    private final int maxExtents;

    public FiemapStructPanama(int maxExtents) {
        this.fullSegment = MemorySegment.allocateNative(BASE_LAYOUT.byteSize() + maxExtents * EXTENT_LAYOUT.byteSize(),
                BASE_LAYOUT.byteAlignment()
        );
        this.baseSegment = fullSegment.asSlice(0, BASE_LAYOUT.byteSize());
        this.extentSegment = fullSegment.asSlice(BASE_LAYOUT.byteSize());
        /*ByteBuf buf = Unpooled.wrappedBuffer(fullSegment.address().toRawLongValue(),
                (int) fullSegment.byteSize(),
                false
        );*/
        this.maxExtents = maxExtents;

        accessorExtentCount.set(baseSegment, maxExtents);
    }

    @Override
    public void setStart(long offset) {
        accessorStart.set(baseSegment, offset);
    }

    @Override
    public void setLength(long length) {
        accessorLength.set(baseSegment, length);
    }

    @Override
    public void setFlags(int flags) {
        accessorFlags.set(baseSegment, flags);
    }

    @Override
    public int getFlags() {
        return (int) accessorFlags.get(baseSegment);
    }

    @Override
    public int getMappedExtents() {
        return (int) accessorMappedExtents.get(baseSegment);
    }

    @Override
    public FiemapExtent getExtent(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Index must be greater or equal 0!");
        }
        if (i >= maxExtents) {
            throw new IndexOutOfBoundsException("Requested extent " + i + " but array is only " + maxExtents + " long!");
        }
        return new FiemapExtentPanama(extentSegment.asSlice(i * EXTENT_LAYOUT.byteSize(), EXTENT_LAYOUT.byteSize()));
    }

    @Override
    public void close() {
        fullSegment.close();
    }

    @Override
    public MemorySegment getMemorySegment() {
        return fullSegment;
    }
}
