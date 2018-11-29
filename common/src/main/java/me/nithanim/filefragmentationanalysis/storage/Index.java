package me.nithanim.filefragmentationanalysis.storage;

import java.util.EnumMap;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import me.nithanim.filefragmentationanalysis.filetypes.Classification;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.filetypes.FileTypeCategory;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileReport;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileSystemUtil;

public class Index {
    private final String path;
    private final FileSystemUtil.OperatingSytem operatingSystem;
    private final long fileSystemMagic;
    private final String fileSystemName;

    private final Buckets all = new Buckets(Classification.DEFAULT);
    private final Buckets known = new Buckets(Classification.DEFAULT);
    private final Buckets unknown = new Buckets(Classification.DEFAULT);
    private final Map<FileTypeCategory, Buckets> byFileTypeCategory = new EnumMap<>(FileTypeCategory.class);
    private final Map<FileType, Buckets> byFileType = new EnumMap<>(FileType.class);

    public Index(String path, FileSystemUtil.OperatingSytem os, long fileSystemMagic, String fileSystemName) {
        this.path = path;
        this.operatingSystem = os;
        this.fileSystemMagic = fileSystemMagic;
        this.fileSystemName = fileSystemName;
    }

    public void add(FileReport fr) {
        add(IndexEntry.of(fr));
    }

    public void add(IndexEntry ie) {
        all.add(ie);

        FileType fileType = ie.getFileType();
        if (fileType != null) {
            known.add(ie);
            byFileType.computeIfAbsent(fileType, k -> new Buckets(Classification.DEFAULT)).add(ie);
            byFileTypeCategory.computeIfAbsent(FileTypeCategory.getCategory(fileType), k -> new Buckets(Classification.DEFAULT)).add(ie);
        } else {
            unknown.add(ie);
        }
    }

    public Stream<IndexEntry> getAll() {
        Stream s = StreamSupport.stream(Spliterators.spliteratorUnknownSize(all.get(0), Spliterator.NONNULL), false);
        for (int i = 1; i < all.getSize(); i++) {
            s = Stream.concat(s, StreamSupport.stream(Spliterators.spliteratorUnknownSize(all.get(i), Spliterator.NONNULL), false));
        }
        return s;
    }

    public long getAllCount() {
        return all.getEntryCount();
    }

    public FileSystemUtil.OperatingSytem getOperatingSystem() {
        return operatingSystem;
    }

    public long getFileSystemMagic() {
        return fileSystemMagic;
    }

    public String getFileSystemName() {
        return fileSystemName;
    }

    public String getPath() {
        return path;
    }

    public Buckets getUnknown() {
        return unknown;
    }

    public Buckets getKnown() {
        return known;
    }

    public Map<FileTypeCategory, Buckets> getByFileTypeCategory() {
        return byFileTypeCategory;
    }

    public Map<FileType, Buckets> getByFileType() {
        return byFileType;
    }
}
