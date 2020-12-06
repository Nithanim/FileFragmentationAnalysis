package me.nithanim.fragmentationstatistics.natives.linux;

import jdk.incubator.foreign.MemorySegment;

public interface Struct extends AutoCloseable {
    MemorySegment getMemorySegment();
}
