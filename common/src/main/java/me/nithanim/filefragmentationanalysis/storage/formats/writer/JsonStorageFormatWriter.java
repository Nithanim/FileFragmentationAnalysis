package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.IndexEntry;

public class JsonStorageFormatWriter implements StorageFormatWriter {
    @Override
    public void write(OutputStream out, Index index) throws IOException {
        if (!(out instanceof BufferedOutputStream)) {
            out = new BufferedOutputStream(out);
        }
        PrintWriter pw = new PrintWriter(out);

        pw.print("{\"version\":");
        writeVersion(pw);
        pw.print(',');
        pw.print("\"os\":\"");
        pw.print(index.getOperatingSystem().name());
        pw.print('\"');
        pw.print(',');
        pw.print("\"fsmagic\": ");
        pw.print(index.getFileSystemMagic());
        pw.print(',');
        pw.print("\"fsname\": ");
        pw.print(index.getFileSystemName() == null ? "null" : '"' + index.getFileSystemName() + '"');
        pw.print(',');
        pw.print("\"count\": ");
        pw.print(index.getAllCount());
        pw.print(',');

        pw.print("\"files\": [");
        Iterator<IndexEntry> it = index.getAll().iterator();
        IndexEntry prev = it.hasNext() ? it.next() : null;
        while (it.hasNext()) {
            writeEntry(prev, pw);
            pw.print(",");
            prev = it.next();
        }
        if (prev != null) {
            writeEntry(prev, pw);
        }
        pw.print("]}");

        pw.flush(); //required because of wrapped stream
    }

    private void writeVersion(PrintWriter pw) throws IOException {
        pw.print("1");
    }

    private void writeEntry(IndexEntry ie, PrintWriter pw) throws IOException {
        pw.print("{");
        pw.print("\"file_type\":");
        pw.print(ie.getFileType() == null ? "null" : "\"" + ie.getFileType() + '"');
        pw.print(',');
        pw.print("\"virtual_file_size\":");
        pw.print(ie.getVirtualFileSize());
        pw.print(',');
        pw.print("\"fragments\":");
        pw.print(ie.getFragments());
        pw.print(',');
        pw.print("\"backtracks\":");
        pw.print(ie.getBacktracks());
        pw.print(',');
        pw.print("\"time_creation\":");
        pw.print(ie.getTimeCreation());
        pw.print(',');
        pw.print("\"time_Last_modified\":");
        pw.print(ie.getTimeLastModified());
        pw.print(',');
        pw.print("\"time_last_accessed\":");
        pw.print(ie.getTimeLastAccessed());
        pw.print("}");
    }
}
