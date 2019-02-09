package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javafx.stage.FileChooser;
import lombok.Value;

public class StorageFormatSelectorFx {
    private static final Map<FileChooser.ExtensionFilter, W> WRITER;
    public static final FileChooser.ExtensionFilter MAIN_EXT_FILTER = new FileChooser.ExtensionFilter("File Fragmentation Index files (*.ffi)", "*.ffi");

    static {
        HashMap<FileChooser.ExtensionFilter, W> wr = new LinkedHashMap<>();
        wr.put(MAIN_EXT_FILTER, new W(StorageFormatType.FRAG, false));
        wr.put(new FileChooser.ExtensionFilter("File Fragmentation Index files gzipped (*.ffi.gz)", "*.ffi.gz"), new W(StorageFormatType.FRAG, true));
        wr.put(new FileChooser.ExtensionFilter("Comma-separated values (*.csv)", "*.csv"), new W(StorageFormatType.CSV, false));
        wr.put(new FileChooser.ExtensionFilter("Comma-separated values gzipped (*.csv.gz)", "*.csv.gz"), new W(StorageFormatType.CSV, true));
        wr.put(new FileChooser.ExtensionFilter("Json (*.json)", "*.json"), new W(StorageFormatType.JSON, false));
        wr.put(new FileChooser.ExtensionFilter("Json gzipped (*.json.gz)", "*.json.gz"), new W(StorageFormatType.JSON, true));
        wr.put(new FileChooser.ExtensionFilter("Wavefront obj (*.obj)", "*.obj"), new W(StorageFormatType.OBJ, false));
        wr.put(new FileChooser.ExtensionFilter("Wavefront obj gzipped (*.obj.gz)", "*.obj.gz"), new W(StorageFormatType.OBJ, true));
        WRITER = Collections.unmodifiableMap(wr);
    }

    public static Set<FileChooser.ExtensionFilter> getAll() {
        return WRITER.keySet();
    }

    public static StorageFormatWriter get(FileChooser.ExtensionFilter ef) {
        W w = WRITER.get(ef);
        return StorageFormatSelector.getWriter(w.getType(), w.isCompressed());
    }

    @Value
    private static class W {
        StorageFormatType type;
        boolean compressed;
    }
}
