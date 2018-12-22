package me.nithanim.filefragmentationanalysis.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.CsvStorageFormatWriter;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.FragStorageFormatWriter;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.GzipStorageFormatWriter;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatWriter;

public enum SaveType {

    FFI(() -> new FragStorageFormatWriter()),
    FFI_GZIP(() -> new GzipStorageFormatWriter(FFI.getStorageFormatWriter().get())),
    CSV(() -> new CsvStorageFormatWriter()),
    CSV_GZIP(() -> new GzipStorageFormatWriter(CSV.getStorageFormatWriter().get()));

    public static Map<String, SaveType> EXT_TO_TYPE;
    public static Map<SaveType, String> TYPE_TO_EXT;

    static {
        HashMap<String, SaveType> m = new HashMap<>();
        m.put("*.ffi.gz", FFI);
        m.put("*.ffi.gz", FFI_GZIP);
        m.put("*.csv", CSV);
        m.put("*.csv.gz", CSV_GZIP);
        EXT_TO_TYPE = Collections.unmodifiableMap(m);
        TYPE_TO_EXT = Collections.unmodifiableMap(m.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e-> e.getKey())));
    }

    private final Supplier<StorageFormatWriter> w;

    private SaveType(Supplier<StorageFormatWriter> w) {
        this.w = w;
    }

    public Supplier<StorageFormatWriter> getStorageFormatWriter() {
        return w;
    }
}
