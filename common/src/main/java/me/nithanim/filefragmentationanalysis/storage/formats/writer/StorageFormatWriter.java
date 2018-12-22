package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.io.IOException;
import java.io.OutputStream;
import me.nithanim.filefragmentationanalysis.storage.Index;

public interface StorageFormatWriter {
    void write(OutputStream out, Index index) throws IOException;
}
