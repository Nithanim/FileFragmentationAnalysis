package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import java.util.ArrayList;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
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

        struct.setStart(0);
        List<Fragment> fragments = new ArrayList<>();
        for (int finishedExtents = 0; finishedExtents < nAllExtents;) {
            la.fillFiemap(fd, struct);
            int nExtents = struct.getMappedExtents();
            long nextStart = 0;
            for (int i = 0; i < nExtents; i++) {
                FiemapExtent e = struct.getExtent(i);
                long inFileByteOffset = e.getLogical();
                long mediumOffsetInBytes = e.getPhysical();
                long extentSizeInBytes = e.getLength();
                Fragment c = new Fragment(inFileByteOffset, mediumOffsetInBytes, extentSizeInBytes);
                fragments.add(c);
                nextStart = inFileByteOffset + extentSizeInBytes;
            }
            struct.setStart(nextStart);
            finishedExtents += nExtents;
        }
        return fragments;
    }

    @Override
    public void close() throws Exception {
        sizeStruct.close();
        struct.close();
    }
}
