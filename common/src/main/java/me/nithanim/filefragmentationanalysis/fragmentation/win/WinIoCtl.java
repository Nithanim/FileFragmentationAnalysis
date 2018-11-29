package me.nithanim.filefragmentationanalysis.fragmentation.win;

public class WinIoCtl {
    private static final int FILE_ANY_ACCESS = 0;
    private static final int FILE_SPECIAL_ACCESS = FILE_ANY_ACCESS;
    private static final int FILE_READ_ACCESS = 0x0001;
    private static final int FILE_WRITE_ACCESS = 0x0002;

    private static final int FILE_DEVICE_BEEP = 0x00000001;
    private static final int FILE_DEVICE_CD_ROM = 0x00000002;
    private static final int FILE_DEVICE_CD_ROM_FILE_SYSTEM = 0x00000003;
    private static final int FILE_DEVICE_CONTROLLER = 0x00000004;
    private static final int FILE_DEVICE_DATALINK = 0x00000005;
    private static final int FILE_DEVICE_DFS = 0x00000006;
    private static final int FILE_DEVICE_DISK = 0x00000007;
    private static final int FILE_DEVICE_DISK_FILE_SYSTEM = 0x00000008;
    private static final int FILE_DEVICE_FILE_SYSTEM = 0x00000009;
    private static final int FILE_DEVICE_INPORT_PORT = 0x0000000a;
    private static final int FILE_DEVICE_KEYBOARD = 0x0000000b;
    private static final int FILE_DEVICE_MAILSLOT = 0x0000000c;
    private static final int FILE_DEVICE_MIDI_IN = 0x0000000d;
    private static final int FILE_DEVICE_MIDI_OUT = 0x0000000e;
    private static final int FILE_DEVICE_MOUSE = 0x0000000f;
    private static final int FILE_DEVICE_MULTI_UNC_PROVIDER = 0x00000010;
    private static final int FILE_DEVICE_NAMED_PIPE = 0x00000011;
    private static final int FILE_DEVICE_NETWORK = 0x00000012;
    private static final int FILE_DEVICE_NETWORK_BROWSER = 0x00000013;
    private static final int FILE_DEVICE_NETWORK_FILE_SYSTEM = 0x00000014;
    private static final int FILE_DEVICE_NULL = 0x00000015;
    private static final int FILE_DEVICE_PARALLEL_PORT = 0x00000016;
    private static final int FILE_DEVICE_PHYSICAL_NETCARD = 0x00000017;
    private static final int FILE_DEVICE_PRINTER = 0x00000018;
    private static final int FILE_DEVICE_SCANNER = 0x00000019;
    private static final int FILE_DEVICE_SERIAL_MOUSE_PORT = 0x0000001a;
    private static final int FILE_DEVICE_SERIAL_PORT = 0x0000001b;
    private static final int FILE_DEVICE_SCREEN = 0x0000001c;
    private static final int FILE_DEVICE_SOUND = 0x0000001d;
    private static final int FILE_DEVICE_STREAMS = 0x0000001e;
    private static final int FILE_DEVICE_TAPE = 0x0000001f;
    private static final int FILE_DEVICE_TAPE_FILE_SYSTEM = 0x00000020;
    private static final int FILE_DEVICE_TRANSPORT = 0x00000021;
    private static final int FILE_DEVICE_UNKNOWN = 0x00000022;
    private static final int FILE_DEVICE_VIDEO = 0x00000023;
    private static final int FILE_DEVICE_VIRTUAL_DISK = 0x00000024;
    private static final int FILE_DEVICE_WAVE_IN = 0x00000025;
    private static final int FILE_DEVICE_WAVE_OUT = 0x00000026;
    private static final int FILE_DEVICE_8042_PORT = 0x00000027;
    private static final int FILE_DEVICE_NETWORK_REDIRECTOR = 0x00000028;
    private static final int FILE_DEVICE_BATTERY = 0x00000029;
    private static final int FILE_DEVICE_BUS_EXTENDER = 0x0000002a;
    private static final int FILE_DEVICE_MODEM = 0x0000002b;
    private static final int FILE_DEVICE_VDM = 0x0000002c;
    private static final int FILE_DEVICE_MASS_STORAGE = 0x0000002d;
    private static final int FILE_DEVICE_SMB = 0x0000002e;
    private static final int FILE_DEVICE_KS = 0x0000002f;
    private static final int FILE_DEVICE_CHANGER = 0x00000030;
    private static final int FILE_DEVICE_SMARTCARD = 0x00000031;
    private static final int FILE_DEVICE_ACPI = 0x00000032;
    private static final int FILE_DEVICE_DVD = 0x00000033;
    private static final int FILE_DEVICE_FULLSCREEN_VIDEO = 0x00000034;
    private static final int FILE_DEVICE_DFS_FILE_SYSTEM = 0x00000035;
    private static final int FILE_DEVICE_DFS_VOLUME = 0x00000036;
    private static final int FILE_DEVICE_SERENUM = 0x00000037;
    private static final int FILE_DEVICE_TERMSRV = 0x00000038;
    private static final int FILE_DEVICE_KSEC = 0x00000039;
    private static final int FILE_DEVICE_FIPS = 0x0000003A;
    private static final int FILE_DEVICE_INFINIBAND = 0x0000003B;

    private static final int METHOD_BUFFERED = 0;
    private static final int METHOD_IN_DIRECT = 1;
    private static final int METHOD_OUT_DIRECT = 2;
    private static final int METHOD_NEITHER = 3;

    private static int CTL_CODE(int deviceType, int function, int method, int access) {
        return ((deviceType) << 16) | ((access) << 14) | ((function) << 2) | (method);
    }

    public static final int FSCTL_GET_RETRIEVAL_POINTERS = CTL_CODE(FILE_DEVICE_FILE_SYSTEM, 28, METHOD_NEITHER, FILE_ANY_ACCESS);

}
