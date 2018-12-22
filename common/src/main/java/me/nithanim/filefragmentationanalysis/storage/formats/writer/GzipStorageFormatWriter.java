package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import me.nithanim.filefragmentationanalysis.storage.Index;

/**
 * Wrapps another {@link StorageFormatWriter} and compresses the data as gzip
 * with deflate algorithm.
 */
public class GzipStorageFormatWriter implements StorageFormatWriter {
    private final StorageFormatWriter wrapped;

    public GzipStorageFormatWriter(StorageFormatWriter wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void write(OutputStream out, Index index) throws IOException {
        GZIPOutputStream o = new GZIPOutputStream(out);
        wrapped.write(o, index);
        o.finish();
        o.flush();
    }
}
