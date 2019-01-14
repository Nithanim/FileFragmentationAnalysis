package me.nithanim.filefragmentationanalysis.storage.formats.reader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.IndexEntry;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

public class FragStorageFormatReader {
    public Index read(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        DataInputStream dis = new DataInputStream(in);
        if (!(dis.read() == 'F'
            && dis.read() == 'R'
            && dis.read() == 'A'
            && dis.read() == 'G')) {
            throw new NotAnIndexFileException();
        }

        int version = (int) readVarint(dis);
        if (version != 1) {
            throw new UnsupportedVersionException("File version is " + version + " but only 1 is supported.");
        }

        FileSystemUtil.OperatingSytem os = FileSystemUtil.OperatingSytem.values()[dis.readByte()];
        long fileSystemMagic = dis.readLong();
        String fileSystemName = dis.readUTF();
        if (fileSystemName.isEmpty()) {
            fileSystemName = null;
        }
        long fileSystemTotalSize = readVarint(dis);
        long fileSystemFreeSize = readVarint(dis);
        long fileSystemBlockSize = readVarint(dis);

        Index index = new Index(
            null,
            os,
            new FileSystemUtil.FileSystemInformation(
                fileSystemName,
                fileSystemMagic,
                fileSystemTotalSize,
                fileSystemFreeSize,
                fileSystemBlockSize
            )
        );

        long count = readVarint(dis);
        for (long i = 0; i < count; i++) {
            index.add(readEntry(dis));
        }
        return index;
    }

    private IndexEntry readEntry(DataInputStream in) throws IOException {
        int ftInt = (int) readVarint(in);
        FileType ft;
        if (ftInt == 0) {
            ft = null;
        } else {
            ft = FileType.values()[ftInt - 1];
        }

        long virtualFileSize = readVarint(in);
        int fragments = (int) readVarint(in);
        int backtracks = (int) readVarint(in);
        long timeCreation = readVarint(in);
        long timeLastModified = readVarint(in);
        long lastTimeAccessed = readVarint(in);
        return new IndexEntry(ft, virtualFileSize, fragments, backtracks, timeCreation, timeLastModified, lastTimeAccessed);
    }

    private long readVarint(DataInputStream in) throws IOException {
        long v = 0;
        for (int i = 0; i < Long.BYTES + 1; i++) {
            long t = in.read();
            v |= ((t & 0b0111_1111) << (7 * i));
            if ((t & 0b1000_0000) == 0) {
                break;
            }
        }
        return v;
    }
}
