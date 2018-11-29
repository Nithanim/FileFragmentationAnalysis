package me.nithanim.filefragmentationanalysis.fragmentation.win;

import java.nio.file.Path;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileSystemUtil;
import me.nithanim.fragmentationstatistics.natives.windows.Winapi;

public class WindowsFileSystemUtil implements FileSystemUtil {
    private final Winapi winapi;

    public WindowsFileSystemUtil(Winapi winapi) {
        this.winapi = winapi;
    }

    @Override
    public FileSystemInformation getFileSystemInfo(Path p) {
        me.nithanim.fragmentationstatistics.natives.windows.FileSystemInformation nfsi = winapi.getFileSystemInformation(p);
        return new FileSystemInformation(nfsi.getFileSystemName(), null);
    }

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.WINDOWS;
    }
}
