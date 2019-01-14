package me.nithanim.fragmentationstatistics.natives;

import java.nio.file.Path;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.Value;

/**
 * General interface to query basic information about the operating and file
 * system.
 */
public interface FileSystemUtil {
    FileSystemInformation getFileSystemInformation(Path p);

    OperatingSytem getOperatingSystem();

    @Value
    public static class FileSystemInformation {
        @Nullable
        String name;
        long magic;
        /**
         * The total size of the file system in bytes.
         */
        long totalSize;
        /**
         * The free size of the file system in bytes.
         */
        long freeSize;
        /**
         * The block size of the file system.
         */
        long blockSize;

        @Override
        public String toString() {
            return FileSystemUtil.class.getSimpleName()
                + "(" + name + ", "
                + (Objects.isNull(magic) ? "null" : "0x" + Long.toHexString(magic)) + ", "
                + Long.toUnsignedString(totalSize) + ", "
                + Long.toUnsignedString(freeSize) + ", "
                + Long.toUnsignedString(blockSize)
                + ")";
        }
    }

    public static enum OperatingSytem {
        LINUX,
        MACOS,
        WINDOWS
    }
}
