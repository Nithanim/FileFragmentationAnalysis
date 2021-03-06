package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import me.nithanim.filefragmentationanalysis.fragmentation.linux.LinuxApiTesthelper;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.JavaTestHelper;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.LinuxTestUtil;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FiemapFragmentationAnalyzerTest {
    @Parameters
    public static Collection<String> data() {
        return Arrays.asList("ftblauncher", "data.008");
    }
    
    private final String file;

    public FiemapFragmentationAnalyzerTest(String file) {
        this.file = file;
    }
    
    @Test
    public void test() throws Exception {
        Path p = Paths.get(file);
        LinuxApiTesthelper la = LinuxTestUtil.linuxApiFromFile(p, p.getFileName().toString());

        LinuxFiemapFileFragmentationAnalyzer analyzer = new LinuxFiemapFileFragmentationAnalyzer(la, 2);
        File file = File.open(la, p);
        List<Fragment> actual = analyzer.analyze(file, la.getBlocksize(file.getFd()));
        List<Fragment> expected = JavaTestHelper.fragmentsFromResourceLinux(p.getFileName().toString());
        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }
}
