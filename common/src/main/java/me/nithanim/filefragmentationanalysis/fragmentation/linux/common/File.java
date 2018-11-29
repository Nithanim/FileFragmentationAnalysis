package me.nithanim.filefragmentationanalysis.fragmentation.linux.common;

import java.io.IOException;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class File implements AutoCloseable {

    public static File open(LinuxApi la, Path path) throws IOException {
        int fd = la.openFileForReading(path);
        return new File(la, path, fd);
    }

    private final LinuxApi la;
    @Getter
    private final Path path;
    @Getter
    private final int fd;

    @Override
    public void close() throws Exception {
        la.closeFile(fd);
    }
}
