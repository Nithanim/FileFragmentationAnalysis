package me.nithanim.filefragmentationanalysis.fragmentation.linux.common;

import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;

/**
 * A special variant of the general analyzer that accepts the block size
 * allowing it to be re-used instead having it to query multiple times.
 */
public interface LinuxSubFileFragmentationAnalyzer extends AutoCloseable {
    public List<Fragment> analyze(File f, int blockSize) throws Exception;
}
