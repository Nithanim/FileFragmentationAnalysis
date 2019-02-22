package me.nithanim.filefragmentationanalysis.fragmentation.linux.fiemap;

import java.nio.file.Path;
import java.nio.file.Paths;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapExtent;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapStruct;
import me.nithanim.fragmentationstatistics.natives.linux.FiemapStructNative;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiNative;

public class FiemapMainTestGenFsRaw {
    public static void main(String[] args) throws Exception {
        LinuxApi la = new LinuxApiNative();

        Path path = Paths.get(args[0]);
        long fstype = la.getFilesystemType(path.toAbsolutePath());
        try (File f = File.open(la, path)) {
            int blockSize = la.getBlocksize(f.getFd());
            System.out.println(Long.toHexString(fstype) + "," + blockSize);

            int nExtents;
            try (FiemapStruct fm = FiemapStructNative.allocate(0)) {
                fm.setStart(0);
                fm.setLength(~0);
                fm.setFlags(0);
                la.fillFiemap(f.getFd(), fm);
                nExtents = fm.getMappedExtents();
            }

            try (FiemapStruct fm = la.allocateFiemapStruct(nExtents)) {
                fm.setStart(0);
                fm.setLength(~0);
                fm.setFlags(0);
                la.fillFiemap(f.getFd(), fm);

                for (int i = 0; i < fm.getMappedExtents(); i++) {
                    FiemapExtent e = fm.getExtent(i);

                    System.out.print(e.getLogical());
                    System.out.print(",");
                    System.out.print(e.getPhysical());
                    System.out.print(",");
                    System.out.print(e.getLength());
                    System.out.print(",");
                    System.out.println(e.getFlags());
                }
            }
        }
    }
}
