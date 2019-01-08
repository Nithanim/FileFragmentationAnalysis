package me.nithanim.filefragmentationanalysis.filetypes;

import lombok.Getter;

public class ExponentialClassification implements Classification {
    private static final int ONE_MEGABYTE = 1024 * 1024;

    @Getter
    private final long separators[];

    /**
     * Calculates the separators that are used to split the files in different
     * buckets based on their size. Bucket 0 contains all below the first
     * separator, 1 all on and between separator 1 and 2, ...
     *
     * In other words: Let s_i be a separator with i as its index and n_s the
     * amount of separators then there are n_s+1 buckets b_j where j is the
     * index of a bucket. A file f with size(f) belongs to the bucket b_j if s_i
     * &gte; s_i and b_j &lt; s_(i+1). If size(f) &lt; s_0 then f belongs to b_0
     * and likewise if size(f) &gte; n_(s-1) f belongs to bucket b_(n_s+1)
     *
     * @param steps
     */
    public ExponentialClassification(int steps) {
        long[] s = new long[steps];
        for (int i = 0; i < steps; i++) {
            s[i] = (1 << i) * ONE_MEGABYTE; //effective s[i] = (int) Math.pow(2, i);
        }
        separators = s;
    }
}
