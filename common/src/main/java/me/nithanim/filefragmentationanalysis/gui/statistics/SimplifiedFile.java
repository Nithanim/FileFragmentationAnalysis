package me.nithanim.filefragmentationanalysis.gui.statistics;

import lombok.Value;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;

@Value
public class SimplifiedFile {
    FileType fileType;
    long size;
    int fragments;
}
