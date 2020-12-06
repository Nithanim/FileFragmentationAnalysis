package me.nithanim.filefragmentationanalysis.fragmentation;

import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.LinuxFileFragmentationAnalyzer;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiPanama;


class LinuxNativeToolbox implements NativeToolbox {
    private final LinuxApiPanama linuxapi;

    public LinuxNativeToolbox() {
        this.linuxapi = new LinuxApiPanama();
    }

    @Override
    public FileFragmentationAnalyzer createFileFragmentationAnalyzer() {
        return new LinuxFileFragmentationAnalyzer(linuxapi);
    }

    @Override
    public FileSystemUtil getFileSystemUtil() {
        return linuxapi;
    }
}
