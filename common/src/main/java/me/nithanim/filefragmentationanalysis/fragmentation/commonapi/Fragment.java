package me.nithanim.filefragmentationanalysis.fragmentation.commonapi;

import lombok.Value;

/**
 * Represents a fragment of a file. All offsets are given in bytes to allow
 * comparison between different operating systems, file systems and storage
 * devices.
 */
@Value
public class Fragment {
    /**
     * Offset from beginning of file in bytes.
     */
    long offset;
    /**
     * The offset from the beginning of the disk in bytes. Like LBA but might
     * not be from start of disk but from partition.
     */
    long diskOffset;
    /**
     * Size of the fragment in bytes.
     */
    long size;
}
