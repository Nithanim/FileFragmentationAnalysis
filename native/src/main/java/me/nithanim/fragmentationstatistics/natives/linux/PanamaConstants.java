package me.nithanim.fragmentationstatistics.natives.linux;

import jdk.incubator.foreign.MemoryLayout;

public class PanamaConstants {
    private static final int _STAT_VER_LINUX = 1;
    public static final int _STAT_VER = _STAT_VER_LINUX;
    private static final int _IOC_NONE = 0;
    private static final int _IOC_SIZEBITS = 14;
    private static final int _IOC_TYPEBITS = 8;
    private static final int _IOC_NRSHIFT = 0;
    private static final int _IOC_NRBITS = 8;
    private static final int _IOC_TYPESHIFT = _IOC_NRSHIFT + _IOC_NRBITS;
    private static final int _IOC_SIZESHIFT = _IOC_TYPESHIFT + _IOC_TYPEBITS;
    private static final int _IOC_DIRSHIFT = _IOC_SIZESHIFT + _IOC_SIZEBITS;
    private static final int _IOC_READ = 2;
    private static final int _IOC_WRITE = 1;
    public static final int FIBMAP = _IO(0x00, 2);
    public static final int FIGETBSZ = _IO(0x00, 2);
    public static final int FS_IOC_FIEMAP = _IOWR('f', 11, FiemapStructPanama.BASE_LAYOUT);


    private static int _IO(int type, int nr) {
        return _IOC(_IOC_NONE, type, nr, 0);
    }

    private static int _IOC(int dir, int type, int nr, int size) {
        return (((dir) << _IOC_DIRSHIFT) | ((type) << _IOC_TYPESHIFT) | ((nr) << _IOC_NRSHIFT) | ((size) << _IOC_SIZESHIFT));
    }

    private static int _IOWR(int type, int nr, MemoryLayout size) {
        return _IOC(_IOC_READ | _IOC_WRITE, (type), (nr), (_IOC_TYPECHECK(size)));
    }

    private static int _IOC_TYPECHECK(MemoryLayout t) {
        return (int) t.byteSize();
    }
}
