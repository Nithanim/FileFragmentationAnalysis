package me.nithanim.fragmentationstatistics.natives.linux;

/**
 * Represents one element from the array of the {@link FiemapStruct}. Shares the
 * memory with the struct that created this object.
 */
public interface FiemapExtent {
    /**
     * This is the last extent in the file. A mapping attempt past this extent
     * will return nothing.
     */
    public static final int FIEMAP_EXTENT_LAST = 0x00000001;
    /**
     * The location of this extent is currently unknown. This may indicate the
     * data is stored on an inaccessible volume or that no storage has been
     * allocated for the file yet.
     */
    public static final int FIEMAP_EXTENT_UNKNOWN = 0x00000002;
    /**
     * This will also set FIEMAP_EXTENT_UNKNOWN. Delayed allocation - while
     * there is data for this extent, its physical location has not been
     * allocated yet.
     *
     */
    public static final int FIEMAP_EXTENT_DELALLOC = 0x00000004;
    /**
     * This extent does not consist of plain filesystem blocks but is encoded
     * (e.g. encrypted or compressed). Reading the data in this extent via I/O
     * to the block device will have undefined results.
     */
    public static final int FIEMAP_EXTENT_ENCODED = 0x00000008;
    /**
     * This will also set FIEMAP_EXTENT_ENCODED The data in this extent has been
     * encrypted by the file system.
     */
    public static final int FIEMAP_EXTENT_DATA_ENCRYPTED = 0x00000080;
    /**
     * Extent offsets and length are not guaranteed to be block aligned.
     */
    public static final int FIEMAP_EXTENT_NOT_ALIGNED = 0x00000100;
    /**
     * This will also set FIEMAP_EXTENT_NOT_ALIGNED Data is located within a
     * meta data block.
     *
     */
    public static final int FIEMAP_EXTENT_DATA_INLINE = 0x00000200;
    /**
     * This will also set FIEMAP_EXTENT_NOT_ALIGNED Data is packed into a block
     * with data from other files.
     */
    public static final int FIEMAP_EXTENT_DATA_TAIL = 0x00000400;
    /**
     * Unwritten extent - the extent is allocated but its data has not been
     * initialized. This indicates the extent's data will be all zero if read
     * through the filesystem but the contents are undefined if read directly
     * from the device.
     */
    public static final int FIEMAP_EXTENT_UNWRITTEN = 0x00000800;
    /**
     * This will be set when a file does not support extents, i.e., it uses a
     * block based addressing scheme. Since returning an extent for each block
     * back to userspace would be highly inefficient, the kernel will try to
     * merge most adjacent blocks into 'extents'.
     */
    public static final int FIEMAP_EXTENT_MERGED = 0x00001000;
    /**
     * Space shared with other files.
     */
    public static final int FIEMAP_EXTENT_SHARED = 0x00002000;
    
    /**
     * logical offset in bytes for the start of the extent
     */
    long getLogical();

    /**
     * physical offset in bytes for the start of the extent
     */
    long getPhysical();

    /**
     * length in bytes for the extent
     */
    long getLength();

    /**
     * FIEMAP_EXTENT_* flags for this extent
     */
    int getFlags();
}
