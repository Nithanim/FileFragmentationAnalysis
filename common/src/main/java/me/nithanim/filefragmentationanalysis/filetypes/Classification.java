package me.nithanim.filefragmentationanalysis.filetypes;

import javax.annotation.Nonnull;

/**
 * Describes the buckets in which the files are separated into.
 */
public interface Classification {
    public static final Classification DEFAULT = new ExponentialClassification(12);

    /**
     * Gets the separators that are used to split the values into the different
     * buckets. There are always (separators + 1) buckets. The separator is used
     * inclusive.
     * <p>
     * Example: With a separator of 5, two buckets &lt;5 and &gt;=5 are
     * available. With separator 5 and 10, three buckets &lt;5, &gt;=5 && &lt;10
     * and &gt;10 are created.
     *
     * @return the array of separators
     */
    @Nonnull
    long[] getSeparators();
}
