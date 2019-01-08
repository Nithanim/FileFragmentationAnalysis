package me.nithanim.filefragmentationanalysis.fragmentation.linux;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import lombok.Value;
import me.nithanim.filefragmentationanalysis.fragmentation.JavaTestHelper;

public class LinuxTestUtil {
    public static LinuxApiTesthelper linuxApiFromFile(Path p, String file) {
        try (Scanner scanner = new Scanner(JavaTestHelper.getResourceLinux(file+ ".fsraw"))) {
            LinuxApiTesthelper la = new LinuxApiTesthelper();
            FileSystemInformation fsi = fileSystemInformationFromString(scanner.nextLine());
            ArrayList<TestExtent> extents = extentsFromScanner(scanner);
            la.expect(p, new LinuxApiTesthelper.FileData(fsi.getMagic(), fsi.getBlockSize(), extents));
            return la;
        }
    }
    
    public static ArrayList<TestExtent> extentsFromScanner(final Scanner scanner) throws NumberFormatException {
        ArrayList<TestExtent> extents = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(",");
            extents.add(new TestExtent(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2]), Integer.parseInt(split[3])));
        }
        return extents;
    }

    public static FileSystemInformation fileSystemInformationFromString(String s) {
        String[] split = s.split(",");
        return new FileSystemInformation(Long.parseLong(split[0], 16), Integer.parseInt(split[1]));
    }

    @Value
    public static class FileSystemInformation {
        long magic;
        int blockSize;
    }
}
