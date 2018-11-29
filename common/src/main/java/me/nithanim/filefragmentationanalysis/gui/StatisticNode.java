package me.nithanim.filefragmentationanalysis.gui;

import lombok.Value;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@Value
public class StatisticNode {
    private String name;
    private DescriptiveStatistics size;
    private DescriptiveStatistics fragments;

    @Override
    public String toString() {
        return name + " (" + size.getN() + ')';
    }
}
