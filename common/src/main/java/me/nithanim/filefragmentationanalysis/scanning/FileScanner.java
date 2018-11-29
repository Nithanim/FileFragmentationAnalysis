package me.nithanim.filefragmentationanalysis.scanning;

import com.sun.jna.Platform;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.nithanim.filefragmentationanalysis.fragmentation.FragmentationAnalyzationException;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileReport;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.LinuxFileFragmentationAnalizer;

@RequiredArgsConstructor
class FileScanner extends SimpleFileVisitor<Path> {
    final ScanningContext context;
    Hack h;
    long dev;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (attrs.isSymbolicLink() || attrs.isOther()) {
            //windows: symbolic links and junctions
            return FileVisitResult.SKIP_SUBTREE;
        } else {
            if (Platform.isLinux()) {
                //needs "--add-opens java.base/java.lang=ALL-UNNAMED" jvm arg
                //alternative is calling native "stat" and get the infos (again)
                if (h == null) {
                    h = commenceHack();
                }
                long dev = h.getDev(attrs);
                if (this.dev == 0) {
                    this.dev = dev;
                } else if (this.dev != dev) { //The device changed so ignore everything below
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }

    @Override
    public FileVisitResult visitFile(Path p, BasicFileAttributes a) {
        if (Thread.interrupted()) {
            Thread.currentThread().interrupt();
            return FileVisitResult.TERMINATE;
        }
        if (!a.isRegularFile()) {
            return FileVisitResult.SKIP_SUBTREE;
        }
        try {
            context.getCurrentPath().set(p);
            List<Fragment> fragments = analyze(p, context.getNativeToolbox().getFileFragmentationAnalyzer());
            FileReport fr = FileReport.of(p, fragments, a.size(), context.getFtr().resolveType(p), a, context.getScantime());
            context.getIndex().add(fr);
            context.getSc().add(fr);
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

    @SneakyThrows
    private static List<Fragment> analyze(Path f, FileFragmentationAnalyzer a) {
        return a.analyze(f);
    }

    @SneakyThrows
    private Hack commenceHack() {
        Class<?> ufs = LinuxFileFragmentationAnalizer.class.getClassLoader().loadClass("sun.nio.fs.UnixFileAttributes");
        Method getDevMethod = ufs.getDeclaredMethod("dev");
        getDevMethod.setAccessible(true);
        Class<?> uasfb = LinuxFileFragmentationAnalizer.class.getClassLoader().loadClass("sun.nio.fs.UnixFileAttributes$UnixAsBasicFileAttributes");
        Field attrsField = uasfb.getDeclaredField("attrs");
        attrsField.setAccessible(true);

        return new Hack() {
            @SneakyThrows
            @Override
            public long getDev(BasicFileAttributes attrs) {
                Object intermediate = attrsField.get(attrs);
                return (long) getDevMethod.invoke(intermediate);
            }
        };
    }

    private static interface Hack {
        long getDev(BasicFileAttributes attrs);
    }
}
