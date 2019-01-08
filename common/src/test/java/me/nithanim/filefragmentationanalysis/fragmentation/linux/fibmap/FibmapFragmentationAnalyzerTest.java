package me.nithanim.filefragmentationanalysis.fragmentation.linux.fibmap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.JavaTestHelper;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.LinuxApiTesthelper;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.LinuxTestUtil;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import org.junit.Assert;

public class FibmapFragmentationAnalyzerTest {
    public void test_data008() throws Exception {
        Path p = Paths.get("data.008");
        LinuxApiTesthelper la = LinuxTestUtil.linuxApiFromFile(p, p.getFileName().toString());

        LinuxFibmapFileFragmentationAnalyzer analyzer = new LinuxFibmapFileFragmentationAnalyzer(la);
        File file = File.open(la, p);
        List<Fragment> actual = analyzer.analyze(file, la.getBlocksize(file.getFd()));
        List<Fragment> expected = JavaTestHelper.fragmentsFromResourceLinux(p.getFileName().toString());
        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }
}
