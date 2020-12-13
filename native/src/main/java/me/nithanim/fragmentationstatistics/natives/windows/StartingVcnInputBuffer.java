package me.nithanim.fragmentationstatistics.natives.windows;

public interface StartingVcnInputBuffer extends AutoCloseable {
    long getStartingVcn();

    void setStartingVcn(long startingVcn);
}
