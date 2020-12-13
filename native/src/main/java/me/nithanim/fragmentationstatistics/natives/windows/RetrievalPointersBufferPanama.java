package me.nithanim.fragmentationstatistics.natives.windows;

import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemorySegment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class RetrievalPointersBufferPanama implements RetrievalPointersBuffer {
    static final GroupLayout LAYOUT = MemoryLayout.ofStruct(//
            MemoryLayout.ofValueBits(32, ByteOrder.nativeOrder()).withName("ExtentCount"),
            MemoryLayout.ofPaddingBits(32),
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("StartingVcn")
    ).withName("RETRIEVAL_POINTERS_BUFFER");

    private static final VarHandle accessorExtentCount = LAYOUT.varHandle(int.class,
            MemoryLayout.PathElement.groupElement("ExtentCount")
    );
    private static final VarHandle accessorStartingVcn = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("StartingVcn")
    );

    private final MemorySegment fullSegment;
    private MemorySegment baseSegment;
    private MemorySegment extentSegment;
    private final int nExtents;

    public RetrievalPointersBufferPanama(int nExtents) {
        if (nExtents < 1) {
            throw new IllegalArgumentException("Minimal number of extents is 1!");
        }
        this.fullSegment = MemorySegment.allocateNative(LAYOUT.byteSize() + nExtents * ExtentPanama.LAYOUT.byteSize(),
                LAYOUT.byteAlignment()
        );
        this.baseSegment = fullSegment.asSlice(0, LAYOUT.byteSize());
        this.extentSegment = fullSegment.asSlice(LAYOUT.byteSize());
        this.nExtents = nExtents;
    }

    @Override
    public native int getExtentCount();

    @Override
    public native long getStartingVcn();

    @Override
    public ExtentPanama getExtent(int idx) {
        if (idx < 0 || idx > nExtents) {
            throw new ArrayIndexOutOfBoundsException("Must be between 0 and " + nExtents + " but got " + idx);
        }
        return new ExtentPanama(extentSegment.asSlice(idx * ExtentPanama.LAYOUT.byteSize(),
                ExtentPanama.LAYOUT.byteSize()
        ));
    }

    @Override
    public int getNumberAllocatedExtents() {
        return nExtents;
    }

    @Override
    public void close() throws Exception {
        fullSegment.close();
    }

    public MemorySegment getMemorySegment() {
        return this.fullSegment;
    }

    @RequiredArgsConstructor
    public static class ExtentPanama implements Extent {
        static final GroupLayout LAYOUT = MemoryLayout.ofStruct(//
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("NextVcn"),
                MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("Lcn")
        );

        private static final VarHandle accessorNextVcn = LAYOUT.varHandle(long.class,
                MemoryLayout.PathElement.groupElement("NextVcn")
        );
        private static final VarHandle accessorLcn = LAYOUT.varHandle(long.class,
                MemoryLayout.PathElement.groupElement("Lcn")
        );

        @Getter
        private final MemorySegment memorySegment;

        @Override
        public long getNextVcn() {
            return (long) accessorNextVcn.get(memorySegment);
        }

        @Override
        public long getLcn() {
            return (long) accessorLcn.get(memorySegment);
        }
    }
}
