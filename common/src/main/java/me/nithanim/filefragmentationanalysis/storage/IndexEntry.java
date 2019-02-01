package me.nithanim.filefragmentationanalysis.storage;

import lombok.Value;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileReport;

/**
 * Represents the scan results of a single file in the {@link Index}.
 */
@Value
public class IndexEntry {
    public static IndexEntry of(FileReport fr) {
        return new IndexEntry(
            fr.getFileType(),
            fr.getVirtualSize(),
            fr.getFragments().size(),
            fr.getBacktracks(),
            isSparse(fr),
            fr.getTimeCreation(),
            fr.getTimeLastModified(),
            fr.getTimeLastModified()
        );
    }

    private static boolean isSparse(FileReport fr) {
        //Normally the allocated (physical) size must be equal or larger.
        //If smaller, it must have unallocated blocks.
        return Long.compareUnsigned(fr.getPhysicalSize(), fr.getVirtualSize()) < 0;
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
    
    boolean sparse;

    long timeCreation;

    long timeLastModified;

    long timeLastAccessed;
}
