package me.nithanim.fragmentationstatistics.natives.windows;


import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemorySegment;
import lombok.Getter;

public class StartingVcnInputBufferPanama implements StartingVcnInputBuffer {

    static final GroupLayout LAYOUT = MemoryLayout.ofStruct(//
            MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder()).withName("StartingVcn"))
            .withName("STARTING_VCN_INPUT_BUFFER");

    private static final VarHandle accessorStartingVcn = LAYOUT.varHandle(long.class,
            MemoryLayout.PathElement.groupElement("StartingVcn")
    );
    @Getter
    private final MemorySegment memorySegment;

    public StartingVcnInputBufferPanama() {
        this.memorySegment = MemorySegment.allocateNative(LAYOUT);
    }

    @Override
    public long getStartingVcn() {
        return (long) accessorStartingVcn.get(memorySegment);
    }

    @Override
    public void setStartingVcn(long startingVcn) {
        accessorStartingVcn.set(memorySegment, startingVcn);
    }

    @Override
    public void close() {
        memorySegment.close();
    }
}
