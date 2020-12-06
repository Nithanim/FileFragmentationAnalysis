package me.nithanim.fragmentationstatistics.natives.linux;

/**
 * Represents the native FiemapStruct.
 */
public interface FiemapStruct extends Struct {
    /**
     * logical offset (inclusive) at which to start mapping (in)
     *
     * fm_start, and fm_length specify the logical range within the file which
     * the process would like mappings for. Extents returned mirror those on
     * disk - that is, the logical offset of the 1st returned extent may start
     * before fm_start, and the range covered by the last returned extent may
     * end after fm_length. All offsets and lengths are in bytes.
     */
    void setStart(long offset);

    /**
     * logical length of mapping which userspace cares about (in)
     *
     * @see #setStart(long)
     */
    void setLength(long length);

    /**
     * flags for request (in/out)
     */
    void setFlags(int flags);

    /**
     * flags for request (in/out)
     */
    int getFlags();

    /**
     * number of extents that were mapped (out)
     */
    public int getMappedExtents();

    FiemapExtent getExtent(int n);
}
