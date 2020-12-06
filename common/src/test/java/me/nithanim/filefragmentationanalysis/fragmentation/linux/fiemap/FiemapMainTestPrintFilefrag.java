package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapExtent;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapStruct;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiPanama;

public class FiemapMainTestPrintFilefrag {
    public static void main(String[] args) throws Exception {
        LinuxApi la = new LinuxApiPanama();

        Path path = Paths.get(args[0]);
        long fstype = la.getFilesystemType(path.toAbsolutePath());
        System.out.println("Fstype: 0x" + Long.toHexString(fstype));
        try (File f = File.open(la, path)) {
            int blockSize = la.getBlocksize(f.getFd());
            System.out.println("Blocksize: " + blockSize);

            int nExtents;
            try (FiemapStruct fm = la.allocateFiemapStruct(0)) {
                fm.setStart(0);
                fm.setLength(~0);
                fm.setFlags(0);
                la.fillFiemap(f.getFd(), fm);
                nExtents = fm.getMappedExtents();
                System.out.println("Number of extents is " + nExtents);
            }
            System.out.println("Assuming sector size of 512 and block size of " + blockSize);
            try (FiemapStruct fm = la.allocateFiemapStruct(nExtents)) {
                fm.setStart(0);
                fm.setLength(~0);
                fm.setFlags(0);
                la.fillFiemap(f.getFd(), fm);
                System.out.format(
                        "%4s: %19s: %23s: %7s: %11s: %5s\n",
                        "ext",
                        "logical_offset",
                        "physical_offset",
                        "length",
                        "expected",
                        "flags"
                );
                String expectedPhysicalPosition = "";
                for (int i = 0; i < fm.getMappedExtents(); i++) {
                    FiemapExtent e = fm.getExtent(i);
                    long extentSizeInBytes = e.getLength();
                    long extentSizeInBlocks = extentSizeInBytes / blockSize;
                    long logicalStartInBytes = e.getLogical();
                    long logicalStartInBlocks = logicalStartInBytes / blockSize;
                    long logicalEndInBlocks = logicalStartInBlocks + extentSizeInBlocks;
                    long mediumOffsetInBytes = e.getPhysical();
                    long physicalStartInBlocks = mediumOffsetInBytes / blockSize;
                    long physicalEndInBlocks = physicalStartInBlocks + extentSizeInBlocks;

                    System.out.format(
                            "%4s:%10s..%8s: %11s..%10s: %7s: %12s %5s\n",
                            i,
                            logicalStartInBlocks,
                            logicalEndInBlocks - 1,
                            physicalStartInBlocks,
                            physicalEndInBlocks - 1,
                            extentSizeInBlocks,
                            expectedPhysicalPosition,
                            flagsToString(e.getFlags())
                    );
                    expectedPhysicalPosition = (physicalStartInBlocks + extentSizeInBlocks) + ":";
                }
            }
        }
    }

    private static String flagsToString(int flags) {
        List<String> strs = new ArrayList<>();
        if ((flags & FiemapConstants.FIEMAP_EXTENT_LAST) != 0) {
            strs.add("last,eof");
        }
        if ((flags & FiemapConstants.FIEMAP_EXTENT_UNKNOWN) != 0) {
            strs.add("unknown");
        }
        if ((flags & FiemapConstants.FIEMAP_EXTENT_ENCODED) != 0) {
            strs.add("encoded");
        }
        return String.join(",", strs);
    }
}
