package me.nithanim.filefragmentationanalysis.filetypes;

import lombok.Getter;

public enum FileType {
    //Images
    JPEG,//(new LinearClassification(1, 1, 9)),
    PNG,
    GIF,
    TIFF,
    //Videos
    FLV, WMV, MKV, AVI, MPEG, MOV, WEBM, MP4,
    WAV, FLAC, WMA, AAC, OGG, M4A,
    SEVEN_ZIP, ZIP, RAR, TAR, GZIP;

    @Getter
    private final Classification classification;

    private FileType() {
        this(Classification.DEFAULT);
    }

    private FileType(Classification classification) {
        this.classification = classification;
    }
}
