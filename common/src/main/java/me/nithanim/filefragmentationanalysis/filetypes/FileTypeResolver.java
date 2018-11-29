package me.nithanim.filefragmentationanalysis.filetypes;

import java.nio.file.Path;
import javax.annotation.Nullable;

public class FileTypeResolver {
    @Nullable
    public FileType resolveType(Path p) {
        String ext = getExt(p).toUpperCase();

        try {
            return FileType.valueOf(ext);
        } catch (IllegalArgumentException ex) {
            switch (ext) {
                case "JPG":
                    return FileType.JPEG;
            }
            return null;
        }
    }

    @Nullable
    public FileTypeCategory resolveTypeCategory(FileType ft) {
        try {
            return FileTypeCategory.getCategory(ft);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private String getExt(Path p) {
        String ext;
        String s = p.getFileName().toString();
        int i = s.lastIndexOf('.');
        if (i == -1) {
            ext = "";
        } else {
            ext = s.substring(i + 1, s.length());
        }
        return ext;
    }
}
