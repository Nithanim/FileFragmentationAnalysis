package me.nithanim.filefragmentationanalysis.storage;

import java.util.EnumMap;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.nithanim.filefragmentationanalysis.filetypes.Classification;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.filetypes.FileTypeCategory;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileReport;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

/**
 * The index is the storage holing the scan results.
 */
public class Index {
    /**
     * The root path of the scan.
     */
    @Nullable
    private final String path;
    @Nonnull
    private final FileSystemUtil.OperatingSytem operatingSystem;
    @Nonnull
    private final FileSystemUtil.FileSystemInformation fileSystemInformation;

    /**
     * The sub-storage holing all scanned files.
     */
    private final Buckets all = new Buckets(Classification.DEFAULT);
    /**
     * The sub-storage holding only the files with known file-type.
     */
    private final Buckets known = new Buckets(Classification.DEFAULT);
    /**
     * The sub-storage holding only the files with unknown file-type.
     */
    private final Buckets unknown = new Buckets(Classification.DEFAULT);
    /**
     * Sub-storage that holds the sub-storages which in turn only hold the
     * scanned file information for the file type category of the map key.
     */
    private final Map<FileTypeCategory, Buckets> byFileTypeCategory = new EnumMap<>(FileTypeCategory.class);
    /**
     * Sub-storage that holds the sub-storages which in turn only hold the
     * scanned file information of the file type of the map key.
     */
    private final Map<FileType, Buckets> byFileType = new EnumMap<>(FileType.class);

    public Index(String path, FileSystemUtil.OperatingSytem os, FileSystemUtil.FileSystemInformation fileSystemInformation) {
        this.path = path;
        this.operatingSystem = os;
        this.fileSystemInformation = fileSystemInformation;
    }

    /**
     * Overload of {@link #add(IndexEntry)}
     *
     * @param fr
     */
    public void add(FileReport fr) {
        add(IndexEntry.of(fr));
    }

    /**
     * Adds a new {@link IndexEntry} to the {@link Index}.
     *
     * @param ie the entry to be added.
     */
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

    /**
     * Returns all entries stored in the index.
     *
     * @return
     */
    public Stream<IndexEntry> getAll() {
        Stream s = StreamSupport.stream(Spliterators.spliteratorUnknownSize(all.get(0), Spliterator.NONNULL), false);
        for (int i = 1; i < all.getNumberOfBuckets(); i++) {
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

    public FileSystemUtil.FileSystemInformation getFileSystemInformation() {
        return fileSystemInformation;
    }

    public String getPath() {
        return path;
    }
}
