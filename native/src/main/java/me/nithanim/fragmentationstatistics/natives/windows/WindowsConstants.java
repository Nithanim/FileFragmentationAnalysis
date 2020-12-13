package me.nithanim.fragmentationstatistics.natives.windows;

public class WindowsConstants {
    public static final int ERROR_FILE_NOT_FOUND = 2;
    public static final int ERROR_ACCESS_DENIED = 5;
    public static final int ERROR_HANDLE_EOF = 38;
    public static final int ERROR_MORE_DATA = 234;

    public static final int FILE_SHARE_READ = 0x00000001;
    public static final int FILE_ATTRIBUTE_NORMAL = 0x00000080;
    public static final int OPEN_EXISTING = 3;

    public static final int FORMAT_MESSAGE_IGNORE_INSERTS = 0x00000200;
    public static final int FORMAT_MESSAGE_FROM_SYSTEM = 0x00001000;

    public static final int LANG_NEUTRAL = 0x00;
    public static final int SUBLANG_DEFAULT = 0x01;

    public static final int METHOD_NEITHER = 3;
    public static final int FILE_DEVICE_FILE_SYSTEM = 0x00000009;
    public static final int FILE_ANY_ACCESS = 0;

    public static final int FSCTL_GET_RETRIEVAL_POINTERS = CTL_CODE(
            FILE_DEVICE_FILE_SYSTEM,
            28,
            METHOD_NEITHER,
            FILE_ANY_ACCESS
    );

    private static int CTL_CODE(int DeviceType, int Function, int Method, int Access) {
        return (((DeviceType) << 16) | ((Access) << 14) | ((Function) << 2) | (Method));
    }

}
