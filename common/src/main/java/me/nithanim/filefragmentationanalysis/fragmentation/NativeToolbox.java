package me.nithanim.filefragmentationanalysis.fragmentation;

import com.sun.jna.Platform;
import java.nio.file.Paths;
import lombok.Value;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.UnsupportedOperatingSystem;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.LinuxFileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl.WindowsFileFragmentationAnalyzer;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiNative;
import me.nithanim.fragmentationstatistics.natives.windows.WinapiNative;

@Value
public class NativeToolbox {
    public static NativeToolbox create() {
        if (Platform.isWindows()) {
            WinapiNative wa = new WinapiNative();
            return new NativeToolbox(
                new WindowsFileFragmentationAnalyzer(
                    wa,
                    wa.allocateStartingVcnInputBuffer(),
                    wa.allocateRetrievalPointersBuffer(100)
                ),
                wa
            );
        } else if (Platform.isMac()) {
            throw new UnsupportedOperatingSystem("MacOS is not supported!");
        } else {
            //For the rest delegate to linux
            LinuxApi la = new LinuxApiNative();
            return new NativeToolbox(
                new LinuxFileFragmentationAnalyzer(la),
                la
            );
        }
    }

    public static void main(String[] args) {
        NativeToolbox nt = NativeToolbox.create();
        System.out.println(nt.getFileSystemUtil().getFileSystemInformation(Paths.get("/")));
    }

    private FileFragmentationAnalyzer fileFragmentationAnalyzer;
    private FileSystemUtil fileSystemUtil;
}
