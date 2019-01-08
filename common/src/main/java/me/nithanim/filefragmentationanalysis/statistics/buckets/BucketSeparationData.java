package me.nithanim.filefragmentationanalysis.statistics.buckets;

import java.util.ArrayList;
import java.util.List;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.storage.IndexEntry;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class BucketSeparationData {
    private final long[] separators;
    private final List<DescriptiveStatistics> buckets;

    public BucketSeparationData(FileType ft) {
        this.separators = ft.getClassification().getSeparators();
        int size = separators.length + 1;
        this.buckets = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            buckets.add(new DescriptiveStatistics());
        }
    }

    public void add(IndexEntry ie) {
        DescriptiveStatistics b = getBucket(ie);
        b.addValue(ie.getFragments());
    }

    private DescriptiveStatistics getBucket(IndexEntry ie) {
        return getBucket(findIndex(ie));
    }

    private DescriptiveStatistics getBucket(int idx) {
        return buckets.get(Math.min(idx, buckets.size() - 1));
    }

    private int findIndex(IndexEntry ie) {
        int i = 0;
        for (; i < separators.length; i++) {
            if (Long.compareUnsigned(ie.getVirtualFileSize(), separators[i]) < 0) {
                return i;
            }
        }
        return i;
    }
}
