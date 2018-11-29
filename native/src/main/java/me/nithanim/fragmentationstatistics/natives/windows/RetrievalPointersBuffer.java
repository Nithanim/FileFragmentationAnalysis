package me.nithanim.fragmentationstatistics.natives.windows;

public interface RetrievalPointersBuffer extends AutoCloseable {
    int getExtentCount();

    long getStartingVcn();

    Extent getExtent(int idx);
    
    long getAddr();

    int getStructSize();
    
    int getNumberAllocatedExtents();

    public interface Extent {
        long getNextVcn();

        long getLcn();
    }
}
