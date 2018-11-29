package me.nithanim.filefragmentationanalysis.formatter;

import me.nithanim.filefragmentationanalysis.statistics.FileStatisticsReport;
import me.nithanim.filefragmentationanalysis.statistics.StatisticalAnalysis;
import me.nithanim.filefragmentationanalysis.statistics.StatisticsCalculator;

public class CsvStatisticFormatter implements StatisticsFormatter {
    @Override
    public String format(FileStatisticsReport v) {
        StringBuilder sb = new StringBuilder();

        sb.append(v.getExtension()).append(';');
        sb.append(printStatistics(v.getSize()));
        sb.append(printStatistics(v.getFragments()));
        return sb.toString();
    }

    private static String printStatistics(StatisticalAnalysis sa) {
        StringBuilder sb = new StringBuilder();
        sb.append(sa.getN()).append(';');
        sb.append(sa.getMin()).append(';');
        sb.append(sa.getMax()).append(';');
        sb.append(sa.getMean()).append(';');
        sb.append(sa.getStandardDeviation()).append(';');
        sb.append(sa.getPercentile25()).append(';');
        sb.append(sa.getPercentile75()).append(';');
        return sb.toString();
    }
}
