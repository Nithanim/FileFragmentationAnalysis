package me.nithanim.filefragmentationanalysis.fragmentation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;

public class JavaTestHelper {

    public static List<Fragment> fragmentsFromResourceWindows(String file) {
        return fragmentsFromResource(getResourceWindows(file + ".fragments"));
    }

    public static List<Fragment> fragmentsFromResourceLinux(String path) {
        return fragmentsFromResource(getResourceLinux(path + ".fragments"));
    }

    public static List<Fragment> fragmentsFromResource(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
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

    public static InputStream getResourceWindows(String file) {
        return JavaTestHelper.class.getResourceAsStream("/nativedata/windows/" + file);
    }

    public static InputStream getResourceLinux(String file) {
        return JavaTestHelper.class.getResourceAsStream("/nativedata/linux/" + file);
    }

    public static InputStream getResource(String path) {
        return JavaTestHelper.class.getResourceAsStream(path);
    }
}
