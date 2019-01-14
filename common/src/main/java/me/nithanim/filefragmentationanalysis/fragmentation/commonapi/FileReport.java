package me.nithanim.filefragmentationanalysis.fragmentation.commonapi;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.Value;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;

@Value
public class FileReport {
    private static final ZoneId UTC = ZoneId.of("UTC").normalized();

    public static FileReport of(Path p, List<Fragment> fragments, long fileSize, FileType fileType, BasicFileAttributes bfa, LocalDate scantime) {
        return of(
            p,
            fragments,
            fileSize,
            fileType,
            bfa.creationTime(),
            bfa.lastModifiedTime(),
            bfa.lastAccessTime(),
            scantime
        );
    }

    public static FileReport of(Path p, List<Fragment> fragments, long fileSize, FileType fileType, FileTime creation, FileTime modified, FileTime accessed, LocalDate scantime) {
        return of(
            p,
            fragments,
            fileSize,
            fileType,
            LocalDateTime.ofInstant(creation.toInstant(), UTC).toLocalDate(),
            LocalDateTime.ofInstant(modified.toInstant(), UTC).toLocalDate(),
            LocalDateTime.ofInstant(accessed.toInstant(), UTC).toLocalDate(),
            scantime
        );
    }

    public static FileReport of(Path p, List<Fragment> fragments, long fileSize, FileType fileType, LocalDate creation, LocalDate modified, LocalDate accessed, LocalDate scantime) {
        //String extension = getFileExtension(path);

        long virtualSize = fileSize;
        long physicalSize = fragments.stream().mapToLong(Fragment::getSize).sum();
        int backtracks = 0;
        for (int i = 0; i < fragments.size() - 1; i++) {
            if (fragments.get(i + 1).getDiskOffset() < fragments.get(i).getDiskOffset()) {
                backtracks++;
            }
        }
        long timeCreation = ChronoUnit.WEEKS.between(creation, scantime);
        long timeLastModified = ChronoUnit.WEEKS.between(modified, scantime);
        long timeLastAccessed = ChronoUnit.WEEKS.between(accessed, scantime);

        return new FileReport(p, fileType, virtualSize, physicalSize, fragments, backtracks, timeCreation, timeLastModified, timeLastAccessed);
    }

    /**
     * The path of the file examined.
     */
    Path path;

    FileType fileType;

    /**
     * The size of the file as it is used basically everwhere.
     */
    long virtualSize;

    /**
     * The size of a file allocated on the disk. It is possible that a file has
     * uninitialized parts which do not need to be saved on disk since it does
     * not contain any data. Other tricks might be used by file system
     * implementation like storing very small files in the MFT itself or
     * multiple files in the same sector.
     */
    long physicalSize;

    /**
     * Information about each fragment.
     */
    List<Fragment> fragments;

    /**
     * The number of times a following fragment comes actually before on disk.
     * Or in other words, the offset to the next is negative.
     */
    int backtracks;

    long timeCreation;

    long timeLastModified;

    long timeLastAccessed;
}
