package me.nithanim.filefragmentationanalysis.fragmentation.linux.common;

import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;

public interface LinuxSubFileFragmentationAnalyzer extends AutoCloseable {
    public List<Fragment> analyze(File f, int blockSize) throws Exception;
}
