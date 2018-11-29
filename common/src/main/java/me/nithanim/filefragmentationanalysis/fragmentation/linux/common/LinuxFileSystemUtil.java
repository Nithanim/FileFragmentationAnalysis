package me.nithanim.filefragmentationanalysis.fragmentation.linux.common;

import java.nio.file.Path;
import lombok.AllArgsConstructor;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileSystemUtil;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;

@AllArgsConstructor
public class LinuxFileSystemUtil implements FileSystemUtil {
    private final LinuxApi la;

    @Override
    public FileSystemInformation getFileSystemInfo(Path p) {
        long magic = la.getFilesystemType(p);
        //TODO FS whose support is programmed via FUSE will have type "fuseblk".
        //Mybe get underlying blkdev and look into code of "lsblk -no name,fstype" (which returns e.g. ntfs) for possible fix.
        LinuxFileSystemType t = LinuxFileSystemType.getFileSystemType(magic);
        return new FileSystemInformation(t.getName(), magic);
    }

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.LINUX;
    }
}
