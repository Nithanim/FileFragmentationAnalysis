package me.nithanim.filefragmentationanalysis.filetypes;

import lombok.Getter;

/**
 * A classification based on the power of two.
 *
 * @see LinearClassification
 */
public class ExponentialClassification implements Classification {
    private static final int ONE_MEGABYTE = 1024 * 1024;

    @Getter
    private final long separators[];

    /**
     * The separators are built with the power of two. The size is in megabytes.
     * <p>
     * Example: With only one step, there would be two buckets. One with all
     * files smaller than one MB and the other with all equal or above.
     * Increasing the number of steps, the separators are 2 MB, 4 MB, 8MB, ...
     *
     * @param steps the number of separators to calculate
     * @see LinearClassification
     */
    public ExponentialClassification(int steps) {
        long[] s = new long[steps];
        for (int i = 0; i < steps; i++) {
            s[i] = (1 << i) * ONE_MEGABYTE; //effective s[i] = (int) Math.pow(2, i);
        }
        separators = s;
    }
}
