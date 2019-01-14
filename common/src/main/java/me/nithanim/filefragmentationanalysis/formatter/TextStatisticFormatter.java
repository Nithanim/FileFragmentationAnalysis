package me.nithanim.filefragmentationanalysis.formatter;

import me.nithanim.filefragmentationanalysis.statistics.FileStatisticsReport;
import me.nithanim.filefragmentationanalysis.statistics.StatisticalAnalysis;

/**
 * Converts the statistics object to a simple text table.
 */
public class TextStatisticFormatter implements StatisticsFormatter {
    @Override
    public String format(FileStatisticsReport v) {
        StringBuilder sb = new StringBuilder();

        sb.append(">>> Fragmentation for ").append(v.getExtension()).append('\n');
        sb.append("Size:\n");
        sb.append(printStatistics(v.getSize()));
        sb.append("Fragments:\n");
        sb.append(printStatistics(v.getFragments()));
        return sb.toString();
    }

    private static String printStatistics(StatisticalAnalysis sa) {
        StringBuilder sb = new StringBuilder();

        String f = "    %11s: ";
        sb.append(String.format(f, "n")).append(sa.getN()).append('\n');
        sb.append(String.format(f, "min")).append(sa.getMin()).append('\n');
        sb.append(String.format(f, "max")).append(sa.getMax()).append('\n');
        sb.append(String.format(f, "mean")).append(sa.getMean()).append('\n');
        sb.append(String.format(f, "stand. dev.")).append(sa.getStandardDeviation()).append('\n');
        sb.append(String.format(f, "25th perc.")).append(sa.getPercentile25()).append('\n');
        sb.append(String.format(f, "75th perc.")).append(sa.getPercentile75()).append('\n');
        return sb.toString();
    }
}
