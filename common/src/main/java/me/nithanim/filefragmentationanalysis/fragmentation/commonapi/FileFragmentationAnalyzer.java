package me.nithanim.filefragmentationanalysis.fragmentation.commonapi;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * A generic (cross-platform) interface for a reader that extracts all
 * interesting information about a given file via the native file system api.
 */
public interface FileFragmentationAnalyzer extends AutoCloseable {
    /**
     * Queries the operation system for a specific file returning all its
     * fragments.
     *
     * @param p the file to analyze
     * @return all fragments of the file
     * @throws IOException
     */
    List<Fragment> analyze(Path p) throws IOException;
}
