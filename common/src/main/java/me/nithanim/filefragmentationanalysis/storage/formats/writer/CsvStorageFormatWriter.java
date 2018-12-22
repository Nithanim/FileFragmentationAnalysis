package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.IndexEntry;

public class CsvStorageFormatWriter implements StorageFormatWriter {
    @Override
    public void write(OutputStream out, Index index) throws IOException {
        PrintWriter pw = new PrintWriter(out);

        pw.print("# version ");
        writeVersion(pw);
        pw.print('\n');
        pw.print("# OS ");
        pw.print(index.getOperatingSystem().name());
        pw.print('\n');
        pw.print("# fsmagic ");
        pw.print(index.getFileSystemMagic());
        pw.print('\n');
        pw.print("# fsname ");
        pw.print(index.getFileSystemName() == null ? "" : index.getFileSystemName());
        pw.print('\n');
        pw.print("# count ");
        pw.print(index.getAllCount());
        pw.print('\n');
        pw.print("FileType,VirtualFileSize,Fragments,Backtracks,TimeCreation,TimeLastModified,TimeLastAccessed");
        pw.print('\n');

        Stream<IndexEntry> s = index.getAll();
        s.forEach(new Consumer<IndexEntry>() {
            @Override
            @SneakyThrows
            public void accept(IndexEntry t) {
                writeEntry(t, pw);
            }
        });
        pw.flush(); //required because of wrapped stream
    }

    private void writeVersion(PrintWriter pw) throws IOException {
        pw.print("1");
    }

    private void writeEntry(IndexEntry ie, PrintWriter pw) throws IOException {
        pw.print(ie.getFileType());
        pw.print(',');
        pw.print(ie.getVirtualFileSize());
        pw.print(',');
        pw.print(ie.getFragments());
        pw.print(',');
        pw.print(ie.getBacktracks());
        pw.print(',');
        pw.print(ie.getTimeCreation());
        pw.print(',');
        pw.print(ie.getTimeLastModified());
        pw.print(',');
        pw.print(ie.getTimeLastAccessed());
        pw.print('\n');
    }
}
