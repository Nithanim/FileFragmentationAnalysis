package me.nithanim.filefragmentationanalysis.fragmentation.linux.fibmap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiNative;

public class FibmapMainTestPrintFragments {

    public static void main(String[] args) throws Exception {
        LinuxApi la = new LinuxApiNative();
        Path path = Paths.get("/home/nithanim/FTB_Launcher.jar");
        try (File f = File.open(la, path)) {
            int blockSize = la.getBlocksize(f.getFd());
            try (LinuxFibmapFileFragmentationAnalyzer a = new LinuxFibmapFileFragmentationAnalyzer(la)) {
                List<Fragment> r = a.analyze(f, blockSize);
                r.forEach(System.out::println);
            }
        }
    }
}
