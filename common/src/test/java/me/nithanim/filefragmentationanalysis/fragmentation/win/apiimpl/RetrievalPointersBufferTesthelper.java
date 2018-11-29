package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.util.List;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBuffer;

public class RetrievalPointersBufferTesthelper implements RetrievalPointersBuffer {
    private List<ExtendedExtentTestHelper> extents;
    private final int maxSize;

    public RetrievalPointersBufferTesthelper(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setExtents(List<ExtendedExtentTestHelper> extents) {
        this.extents = extents;
    }

    @Override
    public int getExtentCount() {
        return extents.size();
    }

    @Override
    public long getStartingVcn() {
        return extents.get(0).getCurrVcn();
    }

    @Override
    public ExtendedExtentTestHelper getExtent(int idx) {
        return extents.get(idx);
    }

    @Override
    public long getAddr() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStructSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public int getNumberAllocatedExtents() {
        return maxSize;
    }
}
