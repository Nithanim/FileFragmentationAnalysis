package me.nithanim.filefragmentationanalysis.filetypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import static me.nithanim.filefragmentationanalysis.filetypes.FileType.*;

public enum FileTypeCategory {
    IMAGE(JPEG, PNG, GIF, TIFF),
    VIDEO(FLV, WMV, MKV, AVI, MPEG, MOV, WEBM, MP4),
    AUDIO(WAV, FLAC, WMA, AAC, OGG, M4A),
    COMPRESSED(SEVEN_ZIP, ZIP, RAR, TAR, GZIP);

    private static final Map<FileType, FileTypeCategory> mapping = new EnumMap<>(FileType.class);

    static {
        for (FileTypeCategory v : FileTypeCategory.values()) {
            v.getFiletypes().forEach(ft -> {
                if (mapping.put(ft, v) != null) {
                    throw new IllegalArgumentException(ft + " is already in use!");
                }
            });
        }
    }

    public static FileTypeCategory getCategory(FileType ft) {
        return mapping.get(ft);
    }

    private final Set<FileType> filetypes;

    private FileTypeCategory(FileType... fts) {
        EnumSet<FileType> s = EnumSet.noneOf(FileType.class);
        s.addAll(Arrays.asList(fts));
        this.filetypes = Collections.unmodifiableSet(s);
    }

    public Set<FileType> getFiletypes() {
        return filetypes;
    }
}
