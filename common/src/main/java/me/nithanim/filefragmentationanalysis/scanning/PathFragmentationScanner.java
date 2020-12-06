package me.nithanim.filefragmentationanalysis.scanning;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ImmediateEventExecutor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.Value;
import me.nithanim.filefragmentationanalysis.fragmentation.FragmentationAnalyzationException;
import me.nithanim.filefragmentationanalysis.fragmentation.NativeToolbox;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathFragmentationScanner {
    private static final Logger log = LoggerFactory.getLogger(PathFragmentationScanner.class);

    public Scan scan(Path path) throws IOException {
        return scan(path, e -> {
        });
    }

    public Scan scan(Path path, Consumer<FragmentationAnalyzationException> onException) {
        DefaultPromise<ScanResult> cf = new DefaultPromise<>(ImmediateEventExecutor.INSTANCE);

        List<FragmentationAnalyzationException> errors = new ArrayList<>();
        NativeToolbox nt = NativeToolbox.create();
        FileSystemUtil.FileSystemInformation fsi = nt.getFileSystemUtil().getFileSystemInformation(path);

        ScanningContext ctx = new ScanningContext(
            nt,
            new Index(
                path.toString(),
                nt.getFileSystemUtil().getOperatingSystem(),
                fsi
            ),
            (e) -> {
                log.error("Exception examining file!", e);
                errors.add(e);
                onException.accept(e);
            }
        );
        Thread t = new Thread(() -> {
            try (FileScanner fileScanner = new FileScanner(ctx)) {
                Files.walkFileTree(path, fileScanner);

                if (!Thread.interrupted()) {
                    cf.setSuccess(new ScanResult(ctx.getIndex(), errors));
                }
            } catch (Exception ex) {
                log.error("Critical exception walking through directories!", ex);
                cf.setFailure(ex);
            }
        });
        t.start();

        cf.addListener((Future<? super ScanResult> future) -> {
            if (future.isCancelled()) {
                t.interrupt();
            }
        });
        return new Scan(cf, ctx.getCurrentPath());
    }

    @Value
    public static class Scan {
        Future<ScanResult> future;
        AtomicReference<Path> currentPath;
    }

    @Value
    public class ScanResult {
        Index index;
        List<FragmentationAnalyzationException> errors;
    }
}
