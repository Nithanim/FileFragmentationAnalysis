package me.nithanim.filefragmentationanalysis.filetypes;

public interface Classification {
    public static final Classification DEFAULT = new ExponentialClassification(10);

    int[] getSeparators();
}
