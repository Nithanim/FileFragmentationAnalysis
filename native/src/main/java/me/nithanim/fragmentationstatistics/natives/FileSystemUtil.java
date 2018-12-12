package me.nithanim.fragmentationstatistics.natives;

import java.nio.file.Path;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Value;

public interface FileSystemUtil {
    FileSystemInformation getFileSystemInformation(Path p);

    OperatingSytem getOperatingSystem();

    @Value
    public static class FileSystemInformation {
        @Nonnull
        String name;
        @Nullable
        Long magic;

        @Override

        public String toString() {
            return FileSystemUtil.class.getSimpleName()
                + "(" + name + ", "
                + (Objects.isNull(magic) ? "null" : "0x" + Long.toHexString(magic))
                + ")";
        }
    }

    public static enum OperatingSytem {
        LINUX,
        MACOS,
        WINDOWS
    }
}
