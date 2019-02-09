package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.util.EnumMap;
import java.util.Set;

public class StorageFormatSelector {
    private static EnumMap<StorageFormatType, StorageFormatWriter> ALL_TYPES = new EnumMap(StorageFormatType.class);

    static {
        ALL_TYPES.put(StorageFormatType.FRAG, new FragStorageFormatWriter());
        ALL_TYPES.put(StorageFormatType.CSV, new CsvStorageFormatWriter());
        ALL_TYPES.put(StorageFormatType.JSON, new JsonStorageFormatWriter());
        ALL_TYPES.put(StorageFormatType.OBJ, new ObjStorageFormatWriter());
    }

    public static Set<StorageFormatType> getAvailableFormatTypes() {
        return ALL_TYPES.keySet();
    }

    public static StorageFormatWriter getWriter(StorageFormatType type, boolean compressed) {
        StorageFormatWriter sfw = ALL_TYPES.get(type);
        return (compressed) ? new GzipStorageFormatWriter(sfw) : sfw;
    }
}
