package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import jdk.incubator.foreign.MemorySegment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.TestExtent;
import java.util.List;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapExtent;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapStruct;

public class FiemapStructTesthelper implements FiemapStruct {
    private final int maxExtents;
    private long start;
    private long length;
    private int flags;
    private List<TestExtent> extents;

    private int allExtents;

    public FiemapStructTesthelper(int maxExtents) {
        this.maxExtents = maxExtents;
    }

    @Override
    public void setStart(long offset) {
        this.start = offset;
    }

    public long getStart() {
        return start;
    }

    @Override
    public void setLength(long length) {
        this.length = length;
    }

    public long getLength() {
        return length;
    }

    @Override
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public int getMappedExtents() {
        if (extents == null) {
            return allExtents;
        } else {
            return extents.size();
        }
    }

    public void setExtents(List<TestExtent> extents) {
        if (extents != null && extents.size() > getMaxExtents()) {
            throw new IllegalArgumentException("Too many extents given!");
        }
        this.extents = extents;
    }

    @Override
    public FiemapExtent getExtent(int n) {
        return extents.get(n);
    }

    @Override
    public void close() throws Exception {
    }

    public int getMaxExtents() {
        return maxExtents;
    }

    public void setAllExtents(int allExtents) {
        this.allExtents = allExtents;
    }

    @Override
    public MemorySegment getMemorySegment() {
        return null;
    }
}
