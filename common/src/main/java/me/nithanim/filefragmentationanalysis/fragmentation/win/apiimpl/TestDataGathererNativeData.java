package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import me.nithanim.fragmentationstatistics.natives.windows.InternalFileSystemInformation;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBufferNative;
import me.nithanim.fragmentationstatistics.natives.windows.StartingVcnInputBufferNative;
import me.nithanim.fragmentationstatistics.natives.windows.Winapi;
import me.nithanim.fragmentationstatistics.natives.windows.WinapiNative;

public class TestDataGathererNativeData {

    public static void main(String[] args) throws IOException {
        Path p = Paths.get(args[0]);

        Winapi winapi = new WinapiNative();
        InternalFileSystemInformation inf = winapi.getInternalFileSystemInformation(p);
        System.out.println(inf.getFileSystemName() + "," + inf.getSectorsPerCluster() + "," + inf.getBytesPerSector());

        StartingVcnInputBufferNative inputBuffer = StartingVcnInputBufferNative.allocate();
        RetrievalPointersBufferNative outputBuffer = RetrievalPointersBufferNative.allocate(10);

        long h = winapi.createFile(p);
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
