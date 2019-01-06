package me.nithanim.filefragmentationanalysis.fragmentation.linux;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.fibmap.LinuxFibmapFileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap.LinuxFiemapFileFragmentationAnalyzer;
import me.nithanim.fragmentationstatistics.natives.NativeCallException;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiNative;

public class LinuxFileFragmentationAnalyzer implements FileFragmentationAnalyzer {
    private final LinuxApi linuxapi;
    private final LinuxFiemapFileFragmentationAnalyzer fiemap = new LinuxFiemapFileFragmentationAnalyzer(100);
    private final LinuxFibmapFileFragmentationAnalyzer fibmap = new LinuxFibmapFileFragmentationAnalyzer();
    private final LinuxDeviceGetter linuxDeviceGetter;

    public LinuxFileFragmentationAnalyzer() {
        this(new LinuxApiNative());
    }

    public LinuxFileFragmentationAnalyzer(LinuxApi linuxapi) {
        this.linuxapi = linuxapi;
        this.linuxDeviceGetter = LinuxDeviceGetter.newInstance(linuxapi);
    }

    @Override
    public List<Fragment> analyze(Path p) throws IOException {
        try (File f = File.open(linuxapi, p)) {
            int blockSize = linuxapi.getBlocksize(f.getFd());
            try {
                return fiemap.analyze(f, blockSize);
            } catch (NativeCallException ex) {
                if (ex.getFunction().equals("ioctl")) {
                    return fibmap.analyze(f, blockSize);
                } else {
                    throw ex;
                }
            }
        } catch (RuntimeException | IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() throws Exception {
        fibmap.close();
        fiemap.close();
        linuxDeviceGetter.close();
    }

    public long getDevice(Path p, BasicFileAttributes bfa) {
        return linuxDeviceGetter.getDev(p, bfa);
    }
}
