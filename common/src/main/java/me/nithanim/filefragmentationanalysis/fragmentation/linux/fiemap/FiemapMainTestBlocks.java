package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import java.nio.file.Path;
import java.nio.file.Paths;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapExtent;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapStruct;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapStructNative;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiNative;

public class FiemapMainTestBlocks {

    public static void main(String[] args) throws Exception {
        LinuxApi la = new LinuxApiNative();

        Path path = Paths.get("/home/nithanim/FTB_Launcher.jar");
        long fstype = la.getFilesystemType(path.toAbsolutePath());
        System.out.println("Fstype: 0x" + Long.toHexString(fstype));
        try (File f = File.open(la, path)) {
            int blockSize = la.getBlocksize(f.getFd());
            System.out.println("Blocksize: " + blockSize);

            int nExtents;
            try (FiemapStruct fm = FiemapStructNative.allocate(0)) {
                fm.setStart(0);
                fm.setLength(~0);
                fm.setFlags(0);
                la.fillFiemap(f.getFd(), fm);
                nExtents = fm.getMappedExtents();
                System.out.println("Number of extents is " + nExtents);
            }
            System.out.println("Assuming sector size of 512 (LBA and sectors are based on this)");
            try (FiemapStruct fm = la.allocateFiemapStruct(nExtents)) {
                fm.setStart(0);
                fm.setLength(~0);
                fm.setFlags(0);
                la.fillFiemap(f.getFd(), fm);
                System.out.format(
                    "%15s %10s %12s %10s %10s %10s\n",
                    "in-file-offset",
                    "block off.",
                    "byte-size",
                    "blocks",
                    "LBA",
                    "sectors"
                );
                for (int i = 0; i < fm.getMappedExtents(); i++) {
                    FiemapExtent e = fm.getExtent(i);
                    long inFileByteOffset = e.getLogical();
                    long mediumOffsetInBytes = e.getPhysical();
                    long mediumLbaStart = mediumOffsetInBytes / 512;
                    long mediumBlockStart = mediumOffsetInBytes / blockSize;
                    long extentSizeInBytes = e.getLength();
                    long extentSizeInSectors = extentSizeInBytes / 512;
                    long extentSizeInBlocks = extentSizeInBytes / blockSize;

                    System.out.format(
                        "%15s %10s %12s %10s %10s %10s\n",
                        inFileByteOffset,
                        mediumBlockStart,
                        extentSizeInBytes,
                        extentSizeInBlocks,
                        mediumLbaStart,
                        extentSizeInSectors
                    );
                }
            }
        }
    }
}
