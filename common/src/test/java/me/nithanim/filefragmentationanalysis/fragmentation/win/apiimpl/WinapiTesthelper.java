package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import me.nithanim.fragmentationstatistics.natives.windows.InternalFileSystemInformation;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.StartingVcnInputBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.Winapi;

@RequiredArgsConstructor
public class WinapiTesthelper implements Winapi {
    private final InternalFileSystemInformation internalFileSystemInformation;

    private final List<ExtendedExtentTestHelper>[] openHandles = new List[5];
    private final Map<Path, Integer> paths = new HashMap<>();
    private int nextHandle = 0;

    public void expect(Path p, List<ExtendedExtentTestHelper> es) {
        paths.put(p, nextHandle);
        openHandles[nextHandle++] = es;
    }

    @Override
    public long createFile(Path p) throws IOException {
        return paths.get(p);
    }

    @Override
    public void closeHandle(long h) {
        //openHandles[(int)Pointer.nativeValue(h.getPointer())].close();
    }

    @Override
    public boolean fetchData(long fileHandle, StartingVcnInputBuffer inputBuffer, RetrievalPointersBuffer outputBuffer) {
        RetrievalPointersBufferTesthelper testOutputBuffer = (RetrievalPointersBufferTesthelper) outputBuffer;
        List<ExtendedExtentTestHelper> allExtents = openHandles[(int) fileHandle];
        ArrayList<ExtendedExtentTestHelper> partialExtents = new ArrayList<>();
        for (int i = 0; i < allExtents.size(); i++) {
            ExtendedExtentTestHelper e = allExtents.get(i);
            if (e.getCurrVcn() < inputBuffer.getStartingVcn()) {
                //For special case when asked VCN is between current and next; rounded down to current
                //Only possible if caller requests an arbitrary VNC, not returned by an api call
                if (e.getNextVcn() > inputBuffer.getStartingVcn()) {
                    partialExtents.add(e);
                } else {
                    continue;
                }
            } else if (e.getCurrVcn() >= inputBuffer.getStartingVcn()) {
                partialExtents.add(e);
            }

            if (partialExtents.size() >= testOutputBuffer.getMaxSize()) {
                //Emulate maximum struct size
                testOutputBuffer.setExtents(partialExtents);
                return false;
            }
        }
        testOutputBuffer.setExtents(partialExtents);
        return true;
    }

    @Override
    public FileSystemInformation getFileSystemInformation(Path p) {
        InternalFileSystemInformation i = getInternalFileSystemInformation(p);
        return new FileSystemInformation(
            i.getFileSystemName(),
            0,
            i.getTotalNumberOfClusters(),
            i.getNumberOfFreeClusters(),
            i.getBytesPerSector() * i.getSectorsPerCluster()
        );
    }

    @Override
    public InternalFileSystemInformation getInternalFileSystemInformation(Path p) {
        return internalFileSystemInformation;
    }

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.WINDOWS;
    }
}
