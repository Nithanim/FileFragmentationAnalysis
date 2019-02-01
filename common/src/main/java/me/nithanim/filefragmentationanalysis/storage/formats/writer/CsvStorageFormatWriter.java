package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.IndexEntry;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

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
        FileSystemUtil.FileSystemInformation fsi = index.getFileSystemInformation();
        pw.print(fsi.getMagic());
        pw.print('\n');
        pw.print("# fsname ");
        pw.print(fsi.getName());
        pw.print('\n');
        pw.print("# WARNING Contrary to other formats, all values ar in BYTES, not blocks for parsers without header parser \n");
        pw.print("# blocksize ");
        pw.print(fsi.getBlockSize());
        pw.print('\n');
        pw.print("# totalsize ");
        pw.print(fsi.getTotalSize());
        pw.print('\n');
        pw.print("# freesize ");
        pw.print(fsi.getFreeSize());
        pw.print('\n');
        pw.print("# datarows ");
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
