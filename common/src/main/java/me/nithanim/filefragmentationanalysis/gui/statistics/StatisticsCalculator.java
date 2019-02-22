package me.nithanim.filefragmentationanalysis.gui.statistics;

import java.util.HashMap;
import java.util.Map;
import lombok.Value;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.filetypes.FileTypeCategory;
import me.nithanim.filefragmentationanalysis.filetypes.FileTypeResolver;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileReport;
import me.nithanim.filefragmentationanalysis.gui.statistics.buckets.BucketSeparationData;
import me.nithanim.filefragmentationanalysis.storage.IndexEntry;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StatisticsCalculator {
    FileTypeResolver ftr = new FileTypeResolver();

    FileStatWrapper overall = new FileStatWrapper();
    FileStatWrapper knownOnly = new FileStatWrapper();
    FileStatWrapper unknownOnly = new FileStatWrapper();

    Map<FileTypeCategory, FileStatWrapper> byFileTypeCategory = new HashMap<>();
    Map<FileType, FileStatWrapper> byFileType = new HashMap<>();
    Map<FileType, BucketSeparationData> byFileTypeClass = new HashMap<>();

    public void add(FileReport fr) {
        add(IndexEntry.of(fr));
    }

    public void add(IndexEntry ie) {
        long size = ie.getVirtualFileSize();
        int fragments = ie.getFragments();
        overall.getSize().addValue(size);
        overall.getFragments().addValue(fragments);

        if (ie.getFileType() != null) {
            knownOnly.getSize().addValue(size);
            knownOnly.getFragments().addValue(fragments);
            {
                FileStatWrapper fsw = byFileType.computeIfAbsent(ie.getFileType(), k -> new FileStatWrapper());
                fsw.getSize().addValue(size);
                fsw.getFragments().addValue(fragments);
            }
            {
                FileTypeCategory ftc = ftr.resolveTypeCategory(ie.getFileType());
                FileStatWrapper fsw = byFileTypeCategory.computeIfAbsent(ftc, k -> new FileStatWrapper());
                fsw.getSize().addValue(size);
                fsw.getFragments().addValue(fragments);
            }
            {
                BucketSeparationData bsd = byFileTypeClass.computeIfAbsent(ie.getFileType(), k -> new BucketSeparationData(k));
                bsd.add(ie);
            }
        } else {
            unknownOnly.getSize().addValue(size);
            unknownOnly.getFragments().addValue(fragments);
        }
    }

    @Value
    public class FileStatWrapper {
        DescriptiveStatistics size = new DescriptiveStatistics();
        DescriptiveStatistics fragments = new DescriptiveStatistics();
    }

    public void calculate() {
    }

    public FileStatWrapper getOverall() {
        return overall;
    }

    public FileStatWrapper getKnownOnly() {
        return knownOnly;
    }

    public FileStatWrapper getUnknownOnly() {
        return unknownOnly;
    }

    public Map<FileType, FileStatWrapper> getByFileType() {
        return byFileType;
    }

    public Map<FileTypeCategory, FileStatWrapper> getByFileTypeCategory() {
        return byFileTypeCategory;
    }
}
