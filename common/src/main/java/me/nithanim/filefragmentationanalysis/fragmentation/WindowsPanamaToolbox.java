package me.nithanim.filefragmentationanalysis.fragmentation;

import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl.WindowsFileFragmentationAnalyzer;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;
import me.nithanim.fragmentationstatistics.natives.windows.WinapiPanama;


class WindowsPanamaToolbox implements NativeToolbox {
    private final WinapiPanama winapi;

    public WindowsPanamaToolbox() {
        this.winapi = new WinapiPanama();
    }

    @Override
    public FileFragmentationAnalyzer createFileFragmentationAnalyzer() {
        return new WindowsFileFragmentationAnalyzer(
                this.winapi,
                this.winapi.allocateStartingVcnInputBuffer(),
                this.winapi.allocateRetrievalPointersBuffer(100)
        );
    }

    @Override
    public FileSystemUtil getFileSystemUtil() {
        return winapi;
    }
}
