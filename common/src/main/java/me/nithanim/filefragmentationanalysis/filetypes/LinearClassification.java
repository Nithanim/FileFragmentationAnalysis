package me.nithanim.filefragmentationanalysis.filetypes;

import lombok.Getter;

public class LinearClassification implements Classification {
    @Getter
    private final int separators[];

    public LinearClassification(int start, int stepSize, int steps) {
        int[] s = new int[steps];
        for (int i = 0; i < steps; i++) {
            s[i] = stepSize * (i + 1) * 1024 * 1024;
        }
        separators = s;
    }
}
