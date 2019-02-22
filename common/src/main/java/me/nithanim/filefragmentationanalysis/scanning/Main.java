package me.nithanim.filefragmentationanalysis.scanning;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import lombok.SneakyThrows;
import me.nithanim.filefragmentationanalysis.fragmentation.FragmentationAnalyzationException;

public class Main {
    @SneakyThrows
    public static void main(String[] args) throws IOException {
        Path p = Paths.get(args[0]);
        PathFragmentationScanner.Scan sl = new PathFragmentationScanner().scan(p);

        while (!sl.getFuture().isDone()) {
            Thread.sleep(500);
            System.out.println(sl.getCurrentPath().get());
        }
        PathFragmentationScanner.ScanResult r = sl.getFuture().get();

        try (PrintWriter pw = new PrintWriter(Files.newOutputStream(Paths.get("logs.csv"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            pw.println("ext;n;min;max;mean;sdv;25perc;75perc;n;min;max;mean;sdv;25perc;75perc;");
            /*r.getStatistics()
                .values()
                .stream()
                .sorted((o1, o2) -> Long.compare(o2.getSize().getN(), o1.getSize().getN()))
                .forEachOrdered((FileStatisticsReport t) -> {
                    System.out.println(tsf.format(t));
                    pw.println(csf.format(t));
                });
             */
        }

        for (FragmentationAnalyzationException e : r.getErrors()) {
            System.out.println(e.getPath() + ": ");
            e.getCause().printStackTrace();
            //System.out.println(e.getKey() + ": " + e.getValue().getMessage());
        }
    }
}
