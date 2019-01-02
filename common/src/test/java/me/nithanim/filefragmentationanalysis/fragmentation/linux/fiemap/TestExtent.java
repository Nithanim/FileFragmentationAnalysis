package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import lombok.Value;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapExtent;

@Value
public class TestExtent implements FiemapExtent {
    long logical;
    long physical;
    long length;
    int flags;
}
