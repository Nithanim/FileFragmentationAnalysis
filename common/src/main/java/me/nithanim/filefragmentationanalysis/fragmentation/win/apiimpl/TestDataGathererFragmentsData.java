package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;

public class TestDataGathererFragmentsData {

    public static void main(String[] args) throws Exception {
        Path p = Paths.get(args[0]);

        try (WindowsFileFragmentationAnalyzer analyzer = new WindowsFileFragmentationAnalyzer()) {
            List<Fragment> fs = analyzer.analyze(p);
            for (Fragment f : fs) {
                System.out.println(f.getOffset() + "," + f.getDiskOffset() + "," + f.getSize());
            }
        }
    }
}
