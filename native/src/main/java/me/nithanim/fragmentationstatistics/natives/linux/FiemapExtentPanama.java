package me.nithanim.fragmentationstatistics.natives.linux;

import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemorySegment;

public class FiemapExtentPanama implements FiemapExtent {
    static final GroupLayout LAYOUT = MemoryLayout.ofStruct(//
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("fe_logical"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("fe_physical"),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("fe_length"),
            MemoryLayout.ofPaddingBits(64 * 2),
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("fe_flags"),
            MemoryLayout.ofPaddingBits(32 * 3)
    ).withName("fiemap_extent");


    private static final VarHandle accessorLogical = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("fe_logical")
    );
    private static final VarHandle accessorPhysical = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("fe_physical")
    );
    private static final VarHandle accessorLength = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("fe_length")
    );
    private static final VarHandle accessorFlags = LAYOUT.varHandle(int.class,
            MemoryLayout.PathElement.groupElement("fe_flags")
    );

    private final MemorySegment memorySegment;

    public FiemapExtentPanama(MemorySegment memorySegment) {
        this.memorySegment = memorySegment;
    }

    @Override
    public long getLogical() {
        return (long) accessorLogical.get(memorySegment);
    }

    @Override
    public long getPhysical() {
        return (long) accessorPhysical.get(memorySegment);
    }

    @Override
    public long getLength() {
        return (long) accessorLength.get(memorySegment);
    }

    @Override
    public int getFlags() {
        return (int) accessorFlags.get(memorySegment);
    }
}
