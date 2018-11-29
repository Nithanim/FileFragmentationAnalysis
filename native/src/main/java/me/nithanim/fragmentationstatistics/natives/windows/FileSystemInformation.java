package me.nithanim.fragmentationstatistics.natives.windows;

import lombok.Value;

@Value
public class FileSystemInformation {
    String fileSystemName;
    int sectorsPerCluster;
    int bytesPerSector;
}
