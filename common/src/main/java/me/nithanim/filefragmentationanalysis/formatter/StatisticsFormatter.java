package me.nithanim.filefragmentationanalysis.formatter;

import me.nithanim.filefragmentationanalysis.statistics.FileStatisticsReport;

/**
 * Pretty prints a statistics format into a (human) readable, save and
 * displayable format.
 */
public interface StatisticsFormatter {
    String format(FileStatisticsReport v);
}
