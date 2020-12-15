package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import jdk.incubator.foreign.MemoryAddress;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil.FileSystemInformation;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.StartingVcnInputBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.Winapi;
import me.nithanim.fragmentationstatistics.natives.windows.WinapiPanama;

public class TestDataGathererNativeDataMain {
    public static void main(String[] args) throws IOException {
        Path p = Paths.get(args[0]);

        Winapi winapi = new WinapiPanama();
        FileSystemInformation fsi = winapi.getFileSystemInformation(p);
        System.out.println(fsi.getName() + "," + fsi.getBlockSize() + "," + fsi.getTotalSize() + "," + fsi.getFreeSize());

        StartingVcnInputBuffer inputBuffer = winapi.allocateStartingVcnInputBuffer();
        RetrievalPointersBuffer outputBuffer = winapi.allocateRetrievalPointersBuffer(10);

        MemoryAddress h = winapi.createFile(p);
        try {
            long currentVcn = 0;
            inputBuffer.setStartingVcn(currentVcn);

            boolean moreData;
            do {
                moreData = !winapi.fetchData(h, inputBuffer, outputBuffer);
                int n = outputBuffer.getExtentCount();
                long nextVcn = 0;
                for (int i = 0; i < n; i++) {
                    RetrievalPointersBuffer.Extent e = outputBuffer.getExtent(i);
                    long lcn = e.getLcn();
                    nextVcn = e.getNextVcn();

                    System.out.println(currentVcn + "," + lcn + "," + nextVcn);
                    currentVcn = nextVcn;
                }
                if (moreData) {
                    inputBuffer.setStartingVcn(nextVcn);
                }
            } while (moreData);
        } finally {
            winapi.closeHandle(h);
        }
    }
}
