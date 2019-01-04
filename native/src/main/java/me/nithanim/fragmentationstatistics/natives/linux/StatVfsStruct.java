package me.nithanim.fragmentationstatistics.natives.linux;

/**
 * The statvfs struct http://man7.org/linux/man-pages/man3/statvfs.3.html
 */
public interface StatVfsStruct extends AutoCloseable {
    /**
     * f_bsize Filesystem block size
     */
    public long getPreferredBlocksize();

    /**
     * f_frsize Fragment size
     */
    long getMinimumBlockSize();

    /**
     * f_blocks Size of fs in f_frsize units
     *
     * https://bugs.debian.org/cgi-bin/bugreport.cgi?bug=671490
     */
    long getTotalNumberOfBlocks();

    /**
     * f_bfree Number of free blocks
     */
    long getNumberOfFreeBlocks();

    /**
     * f_bavail Number of free blocks for unprivileged users
     */
    long getNumberOfFreeBlocksUnprivileged();

    /**
     * f_files Number of inodes
     */
    long getNumberOfInodes();

    /**
     * f_ffree Number of free inodes
     */
    long getNumberOfFreeInodes();

    /**
     * f_favail Number of free inodes for unprivileged users
     */
    long getNumberOfFreeInodesUnprivileged();

    /**
     * f_fsid Filesystem ID
     */
    long getFileSystemId();

    /**
     * f_flag Mount flags
     */
    long getMountFlags();

    /**
     * f_namemax Maximum filename length
     */
    long getMaxFileNameLength();
}
