package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import com.sun.jna.platform.win32.WinNT;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.FragmentationAnalyzationException;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.win.ExtentToFragmentCombiner;
import me.nithanim.fragmentationstatistics.natives.windows.FileSystemInformation;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBufferNative;
import me.nithanim.fragmentationstatistics.natives.windows.StartingVcnInputBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.StartingVcnInputBufferNative;
import me.nithanim.fragmentationstatistics.natives.windows.Winapi;
import me.nithanim.fragmentationstatistics.natives.windows.WinapiNative;

public class WindowsFileFragmentationAnalyzer implements FileFragmentationAnalyzer {
    private final Winapi winapi;
    private final StartingVcnInputBuffer inputBuffer;
    private final RetrievalPointersBuffer outputBuffer;

    private Path lastRoot;
    private int sectorsPerCluster;
    private int bytesPerSector;

    public WindowsFileFragmentationAnalyzer() {
        winapi = new WinapiNative();
        inputBuffer = StartingVcnInputBufferNative.allocate();
        outputBuffer = RetrievalPointersBufferNative.allocate(100);
    }

    public WindowsFileFragmentationAnalyzer(Winapi winapi, StartingVcnInputBuffer inputBuffer, RetrievalPointersBuffer outputBuffer) {
        this.winapi = winapi;
        this.inputBuffer = inputBuffer;
        this.outputBuffer = outputBuffer;
    }

    @Override
    public List<Fragment> analyze(Path p) throws IOException {
        ensureClusterSize(p);
        WinNT.HANDLE h = winapi.createFile(p);

        try {
            long currentVcn = 0;
            inputBuffer.setStartingVcn(currentVcn);

            try {
                ExtentToFragmentCombiner etfc = new ExtentToFragmentCombiner(p, sectorsPerCluster, bytesPerSector);

                boolean moreData;
                do {
                    moreData = !winapi.fetchData(h, inputBuffer, outputBuffer);
                    int n = outputBuffer.getExtentCount();
                    for (int i = 0; i < n; i++) {
                        RetrievalPointersBuffer.Extent e = outputBuffer.getExtent(i);
                        etfc.add(currentVcn, e);
                        currentVcn = e.getNextVcn();
                    }
                    if (moreData) {
                        inputBuffer.setStartingVcn(currentVcn);
                    }
                } while (moreData);

                return etfc.complete();
            } catch (Exception ex) {
                throw new FragmentationAnalyzationException(p, ex);
            }
        } finally {
            winapi.closeHandle(h);
        }
    }

    private static void printRetrievalPointersBuffer(RetrievalPointersBuffer rpb) {
        System.out.println("Extent count: " + rpb.getExtentCount());
        System.out.println("Starting VCN: " + rpb.getStartingVcn());
        for (int i = 0; i < rpb.getExtentCount(); i++) {
            System.out.println("Extent " + i);
            RetrievalPointersBuffer.Extent e = rpb.getExtent(i);
            System.out.println("   Next VCN: " + e.getNextVcn());
            System.out.println("   LCN: " + e.getLcn());
        }
    }

    @Override
    public void close() throws Exception {
        inputBuffer.close();
        outputBuffer.close();
    }

    private void ensureClusterSize(Path p) {
        if (lastRoot == null || !lastRoot.startsWith(p)) {
            Path root = p.getRoot();

            FileSystemInformation fsi = winapi.getFileSystemInformation(p.getRoot());
            bytesPerSector = fsi.getBytesPerSector();
            sectorsPerCluster = fsi.getSectorsPerCluster();

            lastRoot = root;
        }
    }
}
