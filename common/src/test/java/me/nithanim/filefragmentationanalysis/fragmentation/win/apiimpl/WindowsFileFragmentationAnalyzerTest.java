package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.fragmentationstatistics.natives.windows.FileSystemInformation;
import org.junit.Assert;
import org.junit.Test;

public class WindowsFileFragmentationAnalyzerTest {
    @Test
    public void test_starcitizen_datap4k() throws Exception {
        Path p = Paths.get("test");
        WinapiTesthelper winapi = winapiFromResource(p, "/nativedata/windows/starcitizen_data.p4k.fsraw");
        StartingVcnInputBufferTesthelper inputBuffer = new StartingVcnInputBufferTesthelper();
        RetrievalPointersBufferTesthelper outputBuffer = new RetrievalPointersBufferTesthelper(10);

        WindowsFileFragmentationAnalyzer analyzer = new WindowsFileFragmentationAnalyzer(winapi, inputBuffer, outputBuffer);
        List<Fragment> actual = analyzer.analyze(p);
        List<Fragment> expected = fragmentsFromResource("/nativedata/windows/starcitizen_data.p4k.fragments");
        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }

    private WinapiTesthelper winapiFromResource(Path p, String path) {
        WinapiTesthelper winapi;
        try (Scanner scanner = new Scanner(WindowsFileFragmentationAnalyzerTest.class.getResourceAsStream(path))) {
            winapi = new WinapiTesthelper(fileSystemInformationFromString(scanner.nextLine()));
            winapi.expect(p, extentsFromScanner(scanner));
            return winapi;
        }
    }

    private List<Fragment> fragmentsFromResource(String path) {
        try (Scanner scanner = new Scanner(WindowsFileFragmentationAnalyzerTest.class.getResourceAsStream(path))) {
            List<Fragment> l = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(",");
                long offset = Long.parseLong(split[0]);
                long diskOffset = Long.parseLong(split[1]);
                long size = Long.parseLong(split[2]);
                l.add(new Fragment(offset, diskOffset, size));
            }
            return l;
        }
    }

    private ArrayList<ExtendedExtentTestHelper> extentsFromScanner(final Scanner scanner) throws NumberFormatException {
        ArrayList<ExtendedExtentTestHelper> extents = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(",");
            extents.add(new ExtendedExtentTestHelper(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2])));
        }
        return extents;
    }

    private static FileSystemInformation fileSystemInformationFromString(String s) {
        String[] split = s.split(",");
        return new FileSystemInformation(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }
}
