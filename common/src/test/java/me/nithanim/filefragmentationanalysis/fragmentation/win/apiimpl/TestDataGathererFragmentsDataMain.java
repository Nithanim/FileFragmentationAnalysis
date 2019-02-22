package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.fragmentationstatistics.natives.windows.WinapiNative;

public class TestDataGathererFragmentsDataMain {
    public static void main(String[] args) throws Exception {
        Path p = Paths.get(args[0]);
        WinapiNative wa = new WinapiNative();
        try (WindowsFileFragmentationAnalyzer analyzer = new WindowsFileFragmentationAnalyzer(wa)) {
            List<Fragment> fs = analyzer.analyze(p);
            for (Fragment f : fs) {
                System.out.println(f);
            }
        }
    }
}
