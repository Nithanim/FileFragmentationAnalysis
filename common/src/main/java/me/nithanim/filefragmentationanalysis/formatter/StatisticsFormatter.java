package me.nithanim.filefragmentationanalysis.formatter;

import me.nithanim.filefragmentationanalysis.statistics.FileStatisticsReport;
import me.nithanim.filefragmentationanalysis.statistics.StatisticsCalculator;

public interface StatisticsFormatter {
    String format(FileStatisticsReport v);
}
