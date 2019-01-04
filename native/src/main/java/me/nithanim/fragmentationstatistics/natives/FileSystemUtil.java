package me.nithanim.fragmentationstatistics.natives;

import java.nio.file.Path;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.Value;

public interface FileSystemUtil {
    FileSystemInformation getFileSystemInformation(Path p);

    OperatingSytem getOperatingSystem();

    @Value
    public static class FileSystemInformation {
        @Nullable
        String name;
        long magic;
        long totalSize;
        long freeSize;
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
