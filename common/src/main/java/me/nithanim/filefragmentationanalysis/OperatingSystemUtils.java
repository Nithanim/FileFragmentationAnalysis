package me.nithanim.filefragmentationanalysis;

public class OperatingSystemUtils {
    private static final boolean windows;
    private static final boolean mac;
    private static final boolean linux;

    static {
        String osName = System.getProperty("os.name");
        windows = osName.startsWith("Windows");
        mac = osName.startsWith("Mac");
        linux = osName.startsWith("Linux");
    }

    public static boolean isWindows() {
        return windows;
    }

    public static boolean isMac() {
        return mac;
    }

    public static boolean isLinux() {
        return linux;
    }
}
