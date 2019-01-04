package me.nithanim.fragmentationstatistics.natives.windows;

import lombok.Value;

@Value
public class InternalFileSystemInformation {
    String fileSystemName;
    int sectorsPerCluster;
    int bytesPerSector;
    long totalNumberOfClusters;
    long numberOfFreeClusters;
}
