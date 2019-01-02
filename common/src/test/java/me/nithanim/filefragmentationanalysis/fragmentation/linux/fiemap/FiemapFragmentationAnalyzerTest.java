package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lombok.Value;
import me.nithanim.filefragmentationanalysis.fragmentation.JavaTestHelper;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import org.junit.Assert;
import org.junit.Test;

public class FiemapFragmentationAnalyzerTest {
    @Test
    public void test_ftblauncher() throws Exception {
        Path p = Paths.get("test");
        FiemapLinuxApiTesthelper la = linuxApiFromFile(p, "ftblauncher.fsraw");

        LinuxFiemapFileFragmentationAnalyzer analyzer = new LinuxFiemapFileFragmentationAnalyzer(la, 2);
        File file = File.open(la, p);
        List<Fragment> actual = analyzer.analyze(file, la.getBlocksize(file.getFd()));
        List<Fragment> expected = JavaTestHelper.fragmentsFromResourceLinux("ftblauncher");
        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }

    private FiemapLinuxApiTesthelper linuxApiFromFile(Path p, String file) {
        try (Scanner scanner = new Scanner(JavaTestHelper.getResourceLinux(file))) {
            FiemapLinuxApiTesthelper la = new FiemapLinuxApiTesthelper();
            FileSystemInformation fsi = fileSystemInformationFromString(scanner.nextLine());
            ArrayList<TestExtent> extents = extentsFromScanner(scanner);
            la.expect(p, new FiemapLinuxApiTesthelper.FileData(fsi.getMagic(), fsi.getBlockSize(), extents));
            return la;
        }
    }

    private ArrayList<TestExtent> extentsFromScanner(final Scanner scanner) throws NumberFormatException {
        ArrayList<TestExtent> extents = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(",");
            extents.add(new TestExtent(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2]), Integer.parseInt(split[3])));
        }
        return extents;
    }

    private static FileSystemInformation fileSystemInformationFromString(String s) {
        String[] split = s.split(",");
        return new FileSystemInformation(Long.parseLong(split[0], 16), Integer.parseInt(split[1]));
    }
    
    @Value
    private static class FileSystemInformation {
        long magic;
        int blockSize;
    }
}
