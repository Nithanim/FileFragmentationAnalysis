package me.nithanim.filefragmentationanalysis.fragmentation.commonapi;

import java.nio.file.Path;
import java.util.List;
import lombok.Value;

@Value
public class FileFragmentationReport {
    Path path;

    List<Fragment> fragments;
}
