package me.nithanim.filefragmentationanalysis.fragmentation.linux.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum LinuxFileSystemType {
    ADFS_SUPER(0xadf5),
    AFFS_SUPER(0xadff),
    AFS_SUPER(0x5346414f),
    ANON_INODE_FS(0x09041934),
    AUTOFS_SUPER(0x0187),
    BDEVFS(0x62646576),
    BEFS_SUPER(0x42465331),
    BFS(0x1badface),
    BINFMTFS(0x42494e4d),
    BPF_FS(0xcafe4a11),
    BTRFS_SUPER(0x9123683e),
    BTRFS_TEST(0x73727279),
    CGROUP_SUPER(0x27e0eb), /* Cgroup pseudo FS */
    CGROUP2_SUPER(0x63677270), /* Cgroup v2 pseudo FS */
    CIFS(0xff534d42),
    CODA_SUPER(0x73757245),
    COH_SUPER(0x012ff7b7),
    CRAMFS(0x28cd3d45),
    DEBUGFS(0x64626720),
    DEVFS_SUPER(0x1373), /* Linux 2.6.17 and earlier */
    DEVPTS_SUPER(0x1cd1),
    ECRYPTFS_SUPER(0xf15f),
    EFIVARFS(0xde5e81e4),
    EFS_SUPER(0x00414a53),
    EXT_SUPER(0x137d), /* Linux 2.0 and earlier */
    EXT2_OLD_SUPER(0xef51),
    //EXT2_SUPER(0xef53),
    //EXT3_SUPER(0xef53),
    //EXT4_SUPER(0xef53),
    EXT2_OR_HIGHER(0xef53),
    F2FS_SUPER(0xf2f52010),
    FUSE_SUPER(0x65735546),
    FUTEXFS_SUPER(0xbad1dea), /* Unused */
    HFS_SUPER(0x4244),
    HOSTFS_SUPER(0x00c0ffee),
    HPFS_SUPER(0xf995e849),
    HUGETLBFS(0x958458f6),
    ISOFS_SUPER(0x9660),
    JFFS2_SUPER(0x72b6),
    JFS_SUPER(0x3153464a),
    MINIX_SUPER(0x137f), /* original minix FS */
    MINIX_SUPER2(0x138f), /* 30 char minix FS */
    MINIX2_SUPER(0x2468), /* minix V2 FS */
    MINIX2_SUPER2(0x2478), /* minix V2 FS, 30 char names */
    MINIX3_SUPER(0x4d5a), /* minix V3 FS, 60 char names */
    MQUEUE(0x19800202), /* POSIX message queue FS */
    MSDOS_SUPER(0x4d44),
    MTD_INODE_FS(0x11307854),
    NCP_SUPER(0x564c),
    NFS_SUPER(0x6969),
    NILFS_SUPER(0x3434),
    NSFS(0x6e736673),
    NTFS_SB(0x5346544e),
    OCFS2_SUPER(0x7461636f),
    OPENPROM_SUPER(0x9fa1),
    OVERLAYFS_SUPER(0x794c7630),
    PIPEFS(0x50495045),
    PROC_SUPER(0x9fa0), /* /proc FS */
    PSTOREFS(0x6165676c),
    QNX4_SUPER(0x002f),
    QNX6_SUPER(0x68191122),
    RAMFS(0x858458f6),
    REISERFS_SUPER(0x52654973),
    ROMFS(0x7275),
    SECURITYFS(0x73636673),
    SELINUX(0xf97cff8c),
    SMACK(0x43415d53),
    SMB_SUPER(0x517b),
    SOCKFS(0x534f434b),
    SQUASHFS(0x73717368),
    SYSFS(0x62656572),
    SYSV2_SUPER(0x012ff7b6),
    SYSV4_SUPER(0x012ff7b5),
    TMPFS(0x01021994),
    TRACEFS(0x74726163),
    UDF_SUPER(0x15013346),
    UFS(0x00011954),
    USBDEVICE_SUPER(0x9fa2),
    V9FS(0x01021997),
    VXFS_SUPER(0xa501fcf5),
    XENFS_SUPER(0xabba1974),
    XENIX_SUPER(0x012ff7b4),
    XFS_SUPER(0x58465342),;

    private static final Map<Long, LinuxFileSystemType> map;

    static {
        Map<Long, LinuxFileSystemType> m = new HashMap<>();
        for (LinuxFileSystemType v : values()) {
            m.put(v.getMagic(), v);
        }
        map = m;
    }

    @Nullable
    public static LinuxFileSystemType getFileSystemType(long magic) {
        return map.get(magic);
    }

    public static void main(String[] args) {
        HashMap<Long, LinuxFileSystemType> m = new HashMap<>();

        boolean duplicates = false;
        for (LinuxFileSystemType v : values()) {
            if (m.putIfAbsent(v.getMagic(), v) != null) {
                System.out.println("Duplicate magic " + Long.toHexString(v.getMagic()) + " for " + v.name() + " first seen " + m.get(v.getMagic()));
                duplicates = true;
            }
        }
        if (!duplicates) {
            System.out.println("No duplicates found!");
        }

    }

    private final long magic;
    @Nonnull
    private final String name;

    private LinuxFileSystemType(long magic) {
        this.magic = magic;

        if (name().endsWith("_SUPER")) {
            this.name = name().substring(0, name().length() - "_SUPER".length());
        } else {
            this.name = name();
        }
    }

    public long getMagic() {
        return magic;
    }

    public String getName() {
        return name;
    }
}
