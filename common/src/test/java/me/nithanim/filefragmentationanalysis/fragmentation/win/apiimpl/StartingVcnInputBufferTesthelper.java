package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import me.nithanim.fragmentationstatistics.natives.windows.StartingVcnInputBuffer;

public class StartingVcnInputBufferTesthelper implements StartingVcnInputBuffer {
    private long vcn;

    @Override
    public long getStartingVcn() {
        return vcn;
    }

    @Override
    public void setStartingVcn(long startingVcn) {
        this.vcn = startingVcn;
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
}
