package me.nithanim.filefragmentationanalysis.fragmentation;

import me.nithanim.filefragmentationanalysis.OperatingSystemUtils;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.UnsupportedOperatingSystem;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

/**
 * Main entry-point to the native world. The static {@link #create()} is used to
 * create a new instance of this object.
 */
public interface NativeToolbox {
    public static NativeToolbox create() {
        if (OperatingSystemUtils.isWindows()) {
            return new WindowsNativeToolbox();
        } else if (OperatingSystemUtils.isMac()) {
            throw new UnsupportedOperatingSystem("MacOS is not supported!");
        } else {
            //For the rest delegate to linux
            return new LinuxNativeToolbox();
        }
    }

    FileFragmentationAnalyzer createFileFragmentationAnalyzer();

    FileSystemUtil getFileSystemUtil();
}
