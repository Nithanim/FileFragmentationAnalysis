package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.IndexEntry;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

public class FragStorageFormatWriter implements StorageFormatWriter {
    @Override
    public void write(OutputStream out, Index index) throws IOException {
        DataOutputStream o = new DataOutputStream(out);

        writeMagic(o);
        writeVersion(o);

        o.writeByte(index.getOperatingSystem().ordinal());
        FileSystemUtil.FileSystemInformation fsi = index.getFileSystemInformation();
        o.writeLong(fsi.getMagic());
        o.writeUTF(fsi.getName() == null ? "" : fsi.getName());
        long blockSize = fsi.getBlockSize();
        writeVarint(o, blockSize);
        writeVarint(o, Long.divideUnsigned(fsi.getTotalSize(), blockSize));
        writeVarint(o, Long.divideUnsigned(fsi.getFreeSize(), blockSize));

        writeVarint(o, index.getAllCount());

        Stream<IndexEntry> s = index.getAll();
        s.forEach(new Consumer<IndexEntry>() {
            @Override
            @SneakyThrows
            public void accept(IndexEntry t) {
                writeEntry(t, o, blockSize);
            }
        });
        o.flush(); //required because of wrapped stream
    }

    private void writeVersion(DataOutputStream o) throws IOException {
        writeVarint(o, 2);
    }

    private void writeMagic(DataOutputStream o) throws IOException {
        o.write('F');
        o.write('R');
        o.write('A');
        o.write('G');
    }

    private void writeEntry(IndexEntry ie, DataOutputStream out, long blockSize) throws IOException {
        int ftInt;
        if (ie.getFileType() == null) {
            ftInt = 0;
        } else {
            ftInt = ie.getFileType().ordinal() + 1;
        }

        writeVarint(out, ftInt);
        writeVarint(out, Long.divideUnsigned(ie.getVirtualFileSize(), blockSize));
        writeVarint(out, ie.getFragments());
        writeVarint(out, ie.getBacktracks());
        out.writeBoolean(ie.isSparse());
        writeVarint(out, ie.getTimeCreation());
        writeVarint(out, ie.getTimeLastModified());
        writeVarint(out, ie.getTimeLastAccessed());
    }

    /**
     * Writes a variable-sized integer to the stream whose protocol is the same
     * as the one of protobuf by google.
     * <p>
     * Every number is written with minimal amount of bytes possible with where
     * for every byte the MSB is used as indication if more data followes. The
     * remaining 7 bits of of the byte is data.
     * <p>
     * In other words, 7 bits are taken as data and if there is more data then
     * the MSB is set to 1 (or 0 on no more data) and then written to the
     * stream. This is done in a loop until no more data is available (the MSB
     * is set to 0).
     * <p>
     * Reading the saved value is nearly the same. Instead, the data is added to
     * storage and for every MSB == 1, the next byte is processed, otherwise the
     * number is complete. Since on write the lower "byte" of the number is
     * written first, the data has to be shifted i*7 times left and then ORed
     * together with the intermediate result.
     *
     * @param out the stream to write the varint to
     * @param v the varint to write
     * @throws IOException thrown by th write operation by the out stream
     */
    private void writeVarint(DataOutputStream out, long v) throws IOException {
        for (int i = 0; i < Long.BYTES + 1; i++) {
            int t = (int) (v & 0b0111_1111);
            v >>>= 7;
            if (v != 0) {
                out.write(t | 0b1000_0000);
            } else {
                out.write(t);
                break;
            }
        }
    }
}
