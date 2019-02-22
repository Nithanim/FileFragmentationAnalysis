package me.nithanim.filefragmentationanalysis.scanning;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import me.nithanim.filefragmentationanalysis.OperatingSystemUtils;
import me.nithanim.filefragmentationanalysis.filetypes.FileTypeResolver;
import me.nithanim.filefragmentationanalysis.fragmentation.FragmentationAnalyzationException;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileReport;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.LinuxFileFragmentationAnalyzer;

class FileScanner extends SimpleFileVisitor<Path> {
    private final ScanningContext context;
    private final FileFragmentationAnalyzer ffa;
    private final FileTypeResolver ftr;
    private long linuxDevice;

    public FileScanner(ScanningContext context) {
        this.context = context;
        this.ffa = context.getNativeToolbox().getFileFragmentationAnalyzer();
        this.ftr = context.getFtr();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (attrs.isSymbolicLink() || attrs.isOther()) {
            //windows: symbolic links and junctions
            return FileVisitResult.SKIP_SUBTREE;
        } else {
            if (OperatingSystemUtils.isLinux()) {
                //On linux we need to check for mountpoints.
                //We do not know if it is sufficient but for now we check if we changed the device.
                if (!isOnSameLinuxDevice(dir, attrs)) {
                    //The device changed so ignore everything below
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }

    private boolean isOnSameLinuxDevice(Path dir, BasicFileAttributes attrs) {
        LinuxFileFragmentationAnalyzer lffa = (LinuxFileFragmentationAnalyzer) context.getNativeToolbox().getFileFragmentationAnalyzer();
        long dev = lffa.getDevice(dir, attrs);
        if (this.linuxDevice == 0) {
            //init
            this.linuxDevice = dev;
        }

        return this.linuxDevice == dev;
    }

    @Override
    public FileVisitResult visitFile(Path p, BasicFileAttributes bfa) {
        if (Thread.interrupted()) {
            Thread.currentThread().interrupt();
            return FileVisitResult.TERMINATE;
        }
        if (!bfa.isRegularFile()) {
            return FileVisitResult.SKIP_SUBTREE;
        }
        try {
            context.getCurrentPath().set(p);
            List<Fragment> fragments = ffa.analyze(p);
            FileReport fr = FileReport.of(p, fragments, bfa.size(), ftr.resolveType(p), bfa, context.getScantime());
            context.getIndex().add(fr);
        } catch (Exception ex) {
            FragmentationAnalyzationException faex = new FragmentationAnalyzationException(p, ex);
            context.getOnException().accept(faex);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path p, IOException ex) {
        return FileVisitResult.SKIP_SUBTREE;
    }
}
