package me.nithanim.filefragmentationanalysis.filetypes;

import lombok.Getter;

/**
 * An alternative to the {@link ExponentialClassification}.
 */
public class LinearClassification implements Classification {
    @Getter
    private final long separators[];

    public LinearClassification(int start, int stepSize, int steps) {
        long[] s = new long[steps];
        for (int i = 0; i < steps; i++) {
            s[i] = stepSize * (i + 1) * 1024 * 1024;
        }
        separators = s;
    }
}
