package me.nithanim.filefragmentationanalysis.gui.statistics;

import lombok.Value;

@Value
public class FileStatisticsReport {
    String extension;
    StatisticalAnalysis size;
    StatisticalAnalysis fragments;

}
