package me.nithanim.filefragmentationanalysis.storage;

import java.util.Collections;
import java.util.Iterator;
import me.nithanim.filefragmentationanalysis.filetypes.Classification;

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

    private int findIndex(long filesize) {
        int i = 0;
        for (; i < separators.length; i++) {
            if (Long.compareUnsigned(filesize,  separators[i]) < 0) {
                return i;
            }
        }
        return i;
    }

    public int getSize() {
        return occurences.length;
    }

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

    public long getSize(int bucket) {
        ResizableArray<IndexEntry> arr = occurences[bucket];
        if (arr == null) {
            return 0;
        }
        return arr.getSize();
    }

    public Iterator<IndexEntry> get(int bucket) {
        ResizableArray<IndexEntry> arr = occurences[bucket];
        if (arr == null) {
            return Collections.emptyIterator();
        }
        return occurences[bucket].iterator();
    }

    @Override
    public Iterator<Iterator<IndexEntry>> iterator() {
        return new Iterator<Iterator<IndexEntry>>() {
            int idx;

            @Override
            public boolean hasNext() {
                return idx < getSize();
            }

            @Override
            public Iterator<IndexEntry> next() {
                return get(idx);
            }
        };
    }
}
