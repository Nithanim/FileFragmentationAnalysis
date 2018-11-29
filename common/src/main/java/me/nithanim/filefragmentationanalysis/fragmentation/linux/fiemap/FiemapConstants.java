package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

public class FiemapConstants {
    /**
     * sync file data before map
     */
    public static final int FIEMAP_FLAG_SYNC = 0x00000001;
    /**
     * map extended attribute tree
     */
    public static final int FIEMAP_FLAG_XATTR = 0x00000002;
    /**
     * request caching of the extents
     */
    public static final int FIEMAP_FLAG_CACHE = 0x00000004;

    public static final int FIEMAP_FLAGS_COMPAT = (FIEMAP_FLAG_SYNC | FIEMAP_FLAG_XATTR);
    /**
     * Last extent in file.
     */
    public static final int FIEMAP_EXTENT_LAST = 0x00000001;
    /**
     * Data location unknown.
     */
    public static final int FIEMAP_EXTENT_UNKNOWN = 0x00000002;
    /**
     * Location still pending. Sets EXTENT_UNKNOWN.
     */
    public static final int FIEMAP_EXTENT_DELALLOC = 0x00000004;
    /**
     * Data can not be read while fs is unmounted
     */
    public static final int FIEMAP_EXTENT_ENCODED = 0x00000008;
    /**
     * Data is encrypted by fs. Sets EXTENT_NO_BYPASS.
     */
    public static final int FIEMAP_EXTENT_DATA_ENCRYPTED = 0x00000080;
    /**
     * Extent offsets may not be block aligned.
     */
    public static final int FIEMAP_EXTENT_NOT_ALIGNED = 0x00000100;
    /**
     * Data mixed with metadata. Sets EXTENT_NOT_ALIGNED.
     */
    public static final int FIEMAP_EXTENT_DATA_INLINE = 0x00000200;
    /**
     * Multiple files in block. Sets EXTENT_NOT_ALIGNED.
     */
    public static final int FIEMAP_EXTENT_DATA_TAIL = 0x00000400;
    /**
     * Space allocated, but no data (i.e. zero).
     */
    public static final int FIEMAP_EXTENT_UNWRITTEN = 0x00000800;
    /**
     * File does not natively support extents. Result merged for efficiency.
     */
    public static final int FIEMAP_EXTENT_MERGED = 0x00001000;
    /**
     * Space shared with other files.
     */
    public static final int FIEMAP_EXTENT_SHARED = 0x00002000;
}
