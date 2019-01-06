package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.ExtentToFragmentCombiner;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.LinuxSubFileFragmentationAnalyzer;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapExtent;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapStruct;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiNative;

public class LinuxFiemapFileFragmentationAnalyzer implements LinuxSubFileFragmentationAnalyzer {
    /**
     * This struct is exclusively used for querying the number of extents.
     */
    private final FiemapStruct sizeStruct;
    private final FiemapStruct struct;
    private final int allocated;
    private final LinuxApi la;

    public LinuxFiemapFileFragmentationAnalyzer(int nMaxExtents) {
        this(new LinuxApiNative(), nMaxExtents);
    }

    public LinuxFiemapFileFragmentationAnalyzer(LinuxApi la, int nMaxExtents) {
        this.la = la;
        this.allocated = nMaxExtents;
        this.sizeStruct = la.allocateFiemapStruct(0);
        this.sizeStruct.setStart(0);
        this.sizeStruct.setLength(~0);
        this.sizeStruct.setFlags(0);
        this.struct = la.allocateFiemapStruct(nMaxExtents);
        this.struct.setLength(~0);
        this.struct.setFlags(0);
    }

    @Override
    public List<Fragment> analyze(File f, int blockSize) throws Exception {
        int fd = f.getFd();
        la.fillFiemap(fd, sizeStruct);
        int nAllExtents = sizeStruct.getMappedExtents();

        ExtentToFragmentCombiner combiner = new ExtentToFragmentCombiner(blockSize);
        struct.setStart(0);
        for (int finishedExtents = 0; finishedExtents < nAllExtents;) {
            la.fillFiemap(fd, struct);
            int nExtents = struct.getMappedExtents();
            long nextStart = 0;

            FiemapExtent lastExtent = null;
            for (int i = 0; i < nExtents; i++) {
                FiemapExtent e = lastExtent = struct.getExtent(i);
                long logical = e.getLogical();
                long physical = e.getPhysical();
                long length = e.getLength();
                combiner.add(logical, physical, length);
                nextStart = logical + length;
            }
            if (nExtents == 0) { //safeguard if file changed and suddenly no extens are returned anymore
                //just updates number of extents again and if smalle the loop will break
                //so this will keep going just in case nothing is returned one iteration for some reason; you never know
                la.fillFiemap(fd, sizeStruct);
                nAllExtents = sizeStruct.getMappedExtents();
            } else {
                int flags = lastExtent.getFlags();
                //safeguard in case when less extents are returned we can check the last in the array
                if ((flags & FiemapExtent.FIEMAP_EXTENT_LAST) > 0) {
                    break;
                }
            }

            struct.setStart(nextStart);
            finishedExtents += nExtents;
        }
        return combiner.complete();
    }

    @Override
    public void close() throws Exception {
        sizeStruct.close();
        struct.close();
    }
}
