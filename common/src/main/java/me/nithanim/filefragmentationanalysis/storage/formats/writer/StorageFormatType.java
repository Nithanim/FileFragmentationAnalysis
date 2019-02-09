package me.nithanim.filefragmentationanalysis.storage.formats.writer;

public enum StorageFormatType {
    FRAG("ffi"),
    CSV("csv"),
    JSON("json"),
    OBJ("obj");

    private final String extension;

    private StorageFormatType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static StorageFormatType typeFromExtension(String ext) {
        for (StorageFormatType v : values()) {
            if (v.getExtension().equalsIgnoreCase(ext)) {
                return v;
            }
        }
        return null;
    }
}
