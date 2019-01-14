package me.nithanim.filefragmentationanalysis.storage;

import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nonnull;
import me.nithanim.filefragmentationanalysis.filetypes.Classification;

/**
 * Acts as a storage for the different buckets.
 */
public class Buckets implements Iterable<Iterator<IndexEntry>> {
    private final long[] separators;
    private final ResizableArray<IndexEntry>[] occurences;

    public Buckets(Classification c) {
        this.separators = c.getSeparators();
        this.occurences = new ResizableArray[separators.length + 1];
    }

    public void add(IndexEntry ie) {
        int idx = findIndex(ie.getVirtualFileSize());
        ResizableArray<IndexEntry> arr = occurences[idx];
        if (arr == null) {
            occurences[idx] = arr = new ResizableArray<>();
        }
        arr.add(ie);
    }

    /**
     * Calculates the index in the bucket array.
     *
     * @param filesize
     * @return
     */
    private int findIndex(long filesize) {
        int i = 0;
        for (; i < separators.length; i++) {
            if (Long.compareUnsigned(filesize, separators[i]) < 0) {
                return i;
            }
        }
        return i;
    }

    public int getNumberOfBuckets() {
        return occurences.length;
    }

    /**
     * Gets the number of all entries stored in this storage array.
     *
     * @return
     */
    public long getEntryCount() {
        long count = 0;
        for (int i = 0; i < occurences.length; i++) {
            ResizableArray<IndexEntry> o = occurences[i];
            if (o != null) {
                count += occurences[i].getSize();
            }
        }
        return count;
    }

    /**
     * Gets the number of stored entries in a specific bucket.
     *
     * @param bucket
     * @return
     */
    public long getSize(int bucket) {
        ResizableArray<IndexEntry> arr = occurences[bucket];
        if (arr == null) {
            return 0;
        }
        return arr.getSize();
    }

    /**
     * Returns an iterator for a specific bucket.
     *
     * @param bucket
     * @return
     */
    @Nonnull
    public Iterator<IndexEntry> get(int bucket) {
        ResizableArray<IndexEntry> arr = occurences[bucket];
        if (arr == null) {
            return Collections.emptyIterator();
        }
        return occurences[bucket].iterator();
    }

    /**
     * Returns an iterator for the storage array (all containing buckets).
     *
     * @return
     */
    @Override
    @Nonnull
    public Iterator<Iterator<IndexEntry>> iterator() {
        return new Iterator<Iterator<IndexEntry>>() {
            int idx;

            @Override
            public boolean hasNext() {
                return idx < getNumberOfBuckets();
            }

            @Override
            public Iterator<IndexEntry> next() {
                return get(idx);
            }
        };
    }
}
