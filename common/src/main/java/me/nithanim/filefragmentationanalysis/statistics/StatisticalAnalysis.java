package me.nithanim.filefragmentationanalysis.statistics;

import lombok.Value;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@Value
public class StatisticalAnalysis {
    public static StatisticalAnalysis from(DescriptiveStatistics ds) {
        return new StatisticalAnalysis(ds.getN(), ds.getMin(), ds.getMax(), ds.getMean(), ds.getStandardDeviation(), ds.getPercentile(25), ds.getPercentile(75));
    }
    long n;
    double min;
    double max;
    double mean;
    double standardDeviation;
    double percentile25;
    double percentile75;
}
