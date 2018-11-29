package me.nithanim.filefragmentationanalysis.fragmentation.commonapi;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * A generic (cross-platform) interface for a reader that extracts all
 * interesting information about a given file via the native filesystem api.
 */
public interface FileFragmentationAnalyzer extends AutoCloseable {
    List<Fragment> analyze(Path p) throws IOException;
}
