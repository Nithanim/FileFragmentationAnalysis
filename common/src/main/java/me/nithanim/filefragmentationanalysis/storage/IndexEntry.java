package me.nithanim.filefragmentationanalysis.storage;

import lombok.Value;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileReport;

@Value
public class IndexEntry {
    public static IndexEntry of(FileReport fr) {
        return new IndexEntry(
            fr.getFileType(),
            fr.getVirtualSize(),
            fr.getFragments().size(),
            fr.getBacktracks(),
            fr.getTimeCreation(),
            fr.getTimeLastModified(),
            fr.getTimeLastModified()
        );
    }

    FileType fileType;

    long virtualFileSize;

    /**
     * Number of fragments.
     */
    int fragments;
    /**
     * Number of times the next fragment actually comes before on disk.
     */
    int backtracks;

    long timeCreation;

    long timeLastModified;

    long timeLastAccessed;
}
