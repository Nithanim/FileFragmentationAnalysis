package me.nithanim.filefragmentationanalysis.scanning;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.Value;
import me.nithanim.filefragmentationanalysis.filetypes.FileTypeResolver;
import me.nithanim.filefragmentationanalysis.fragmentation.FragmentationAnalyzationException;
import me.nithanim.filefragmentationanalysis.fragmentation.NativeToolbox;
import me.nithanim.filefragmentationanalysis.statistics.StatisticsCalculator;
import me.nithanim.filefragmentationanalysis.storage.Index;

@Value
public class ScanningContext {
    FileTypeResolver ftr = new FileTypeResolver();
    AtomicReference<Path> currentPath = new AtomicReference<>();
    StatisticsCalculator sc = new StatisticsCalculator();
    LocalDate scantime = LocalDate.now();
    NativeToolbox nativeToolbox;
    Index index;
    Consumer<FragmentationAnalyzationException> onException;
}
