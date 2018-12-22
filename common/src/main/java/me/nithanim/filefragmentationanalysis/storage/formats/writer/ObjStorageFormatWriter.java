package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.IndexEntry;

public class ObjStorageFormatWriter implements StorageFormatWriter {
    @Override
    public void write(OutputStream out, Index index) throws IOException {
        PrintWriter pw = new PrintWriter(out);

        int counter = 0;
        Iterator<IndexEntry> it = index.getAll().iterator();

        while (it.hasNext()) {
            if (!writeEntry(it, pw)) {
                break;
            }
            if (!writeEntry(it, pw)) {
                break;
            }
            if (!writeEntry(it, pw)) {
                break;
            }

            pw.print("f ");
            pw.print(counter);
            pw.print(' ');
            pw.print(counter + 1);
            pw.print(' ');
            pw.print(counter + 2);
            pw.print('\n');
            counter += 3;
        }
        pw.flush(); //required because of wrapped stream
    }

    private boolean writeEntry(Iterator<IndexEntry> it, PrintWriter pw) throws IOException {
        if (!it.hasNext()) {
            return false;
        }
        double a = it.next().getVirtualFileSize();
        if (!it.hasNext()) {
            return false;
        }
        double b = it.next().getVirtualFileSize();
        if (!it.hasNext()) {
            return false;
        }
        double c = it.next().getVirtualFileSize();
        pw.print("v ");
        pw.print(a);
        pw.print(' ');
        pw.print(b);
        pw.print(' ');
        pw.print(c);
        pw.print('\n');
        return true;
    }

    private void writeEntry(IndexEntry ie, PrintWriter pw) throws IOException {
        pw.print("v ");
        pw.print((double) ie.getVirtualFileSize());
        pw.print(' ');
        pw.print((double) ie.getFragments());
        pw.print(' ');
        pw.print((double) ie.getBacktracks());
        pw.print('\n');
    }
}
