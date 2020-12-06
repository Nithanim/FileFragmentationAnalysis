package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiPanama;

public class FiemapMainTestPrintFragments {
    public static void main(String[] args) throws Exception {
        LinuxApi la = new LinuxApiPanama();
        Path path = Paths.get(args[0]);
        try (File f = File.open(la, path)) {
            int blockSize = la.getBlocksize(f.getFd());
            try (LinuxFiemapFileFragmentationAnalyzer a = new LinuxFiemapFileFragmentationAnalyzer(la, 5)) {
                List<Fragment> r = a.analyze(f, blockSize);
                r.forEach(System.out::println);
            }
        }
    }
}
