package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.incubator.foreign.MemoryAddress;
import lombok.RequiredArgsConstructor;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.StartingVcnInputBuffer;
import me.nithanim.fragmentationstatistics.natives.windows.Winapi;

@RequiredArgsConstructor
public class WinapiTesthelper implements Winapi {
    private final FileSystemInformation fileSystemInformation;
    private final Map<MemoryAddress, List<ExtendedExtentTestHelper>> openHandles = new HashMap<>();
    private final Map<Path, MemoryAddress> paths = new HashMap<>();
    private int nextHandle = 0;

    public void expect(Path p, List<ExtendedExtentTestHelper> es) {
        MemoryAddress next = MemoryAddress.ofLong(nextHandle);
        paths.put(p, next);
        openHandles.put(next, es);
    }

    @Override
    public MemoryAddress createFile(Path p) throws IOException {
        return paths.get(p);
    }

    @Override
    public void closeHandle(MemoryAddress h) throws IOException {
        openHandles.remove(h);
        paths.values().remove(h);
    }

    @Override
    public boolean fetchData(MemoryAddress fileHandle, StartingVcnInputBuffer inputBuffer, RetrievalPointersBuffer outputBuffer) {
        RetrievalPointersBufferTesthelper testOutputBuffer = (RetrievalPointersBufferTesthelper) outputBuffer;
        List<ExtendedExtentTestHelper> allExtents = openHandles.get(fileHandle);
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
        return fileSystemInformation;
    }

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.WINDOWS;
    }

    @Override
    public StartingVcnInputBuffer allocateStartingVcnInputBuffer() {
        return new StartingVcnInputBufferTesthelper();
    }

    @Override
    public RetrievalPointersBuffer allocateRetrievalPointersBuffer(int nElements) {
        return new RetrievalPointersBufferTesthelper(nElements);
    }
}
