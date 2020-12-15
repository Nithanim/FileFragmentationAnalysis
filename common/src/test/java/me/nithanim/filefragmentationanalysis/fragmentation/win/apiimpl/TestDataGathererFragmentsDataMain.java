package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.fragmentationstatistics.natives.windows.WinapiPanama;

public class TestDataGathererFragmentsDataMain {
    public static void main(String[] args) throws Exception {
        Path p = Paths.get(args[0]);
        WinapiPanama wa = new WinapiPanama();
        try (WindowsFileFragmentationAnalyzer analyzer = new WindowsFileFragmentationAnalyzer(wa)) {
            List<Fragment> fs = analyzer.analyze(p);
            for (Fragment f : fs) {
                System.out.println(f);
            }
        }
    }
}
