package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapStruct;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.StatStruct;
import org.junit.Assert;

@RequiredArgsConstructor
public class FiemapLinuxApiTesthelper implements LinuxApi {
    private final FileData[] openHandles = new FileData[10];
    private final Map<Path, Integer> handleMap = new HashMap<>();
    private final Map<Path, FileData> dataMap = new HashMap<>();
    private int nextHandle = 4;

    public void expect(Path p, FileData fd) {
        handleMap.put(p, nextHandle);
        openHandles[nextHandle++] = fd;
    }

    @Override
    public int openFileForReading(Path path) throws IOException {
        return handleMap.get(path);
    }

    @Override
    public void closeFile(int fd) {
        if (openHandles[fd] == null) {
            Assert.fail("Handle does not exist!");
        }
        openHandles[fd] = null;
    }

    @Override
    public void fillFiemap(int fileHandle, FiemapStruct _fs) {
        FiemapStructTesthelper fs = (FiemapStructTesthelper) _fs;

        FileData fd = openHandles[(int) fileHandle];
        List<TestExtent> allExtents = fd.getExtents();
        ArrayList<TestExtent> partialExtents = new ArrayList<>();
        if (fs.getMaxExtents() == 0) {
            fs.setAllExtents(allExtents.size());
            fs.setExtents(null);
            return; //handled in struct
        }

        for (int i = 0; i < allExtents.size(); i++) {
            TestExtent e = allExtents.get(i);

            if (e.getLogical() < fs.getStart()) {
                //For special case when asked logical is between current and next; rounded down to current
                //Only possible case if caller requests an arbitrary logical, not returned by an api call
                if (e.getLogical() + e.getLength() > fs.getStart()) {
                    partialExtents.add(e);
                } else {
                    continue;
                }
            } else if (e.getLogical() >= fs.getStart()) {
                partialExtents.add(e);
            }

            if (partialExtents.size() >= fs.getMaxExtents()) {
                //Emulate maximum struct size
                fs.setExtents(partialExtents);
                return;
            }
        }
        //If all extents fit in the struct
        fs.setExtents(partialExtents);
    }

    @Override
    public FileSystemInformation getFileSystemInformation(Path p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OperatingSytem getOperatingSystem() {
        return OperatingSytem.LINUX;
    }

    @Override
    public int getBlocksize(int fd) {
        return openHandles[fd].getBlockSize();
    }

    @Override
    public long getFilesystemType(Path path) {
        return dataMap.get(path).getFileSystemMagic();
    }

    @Override
    public void fstat(int fd, StatStruct stat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stat(Path file, StatStruct stat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FiemapStruct allocateFiemapStruct(int maxExtents) {
        return new FiemapStructTesthelper(maxExtents);
    }

    @Override
    public int fibmap(int fd, int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Value
    public static class FileData {
        long fileSystemMagic;
        int blockSize;
        List<TestExtent> extents;
    }
}
