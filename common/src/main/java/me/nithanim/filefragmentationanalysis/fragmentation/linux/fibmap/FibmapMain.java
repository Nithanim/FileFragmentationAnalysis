package me.nithanim.filefragmentationanalysis.fragmentation.linux.fibmap;

import java.nio.file.Path;
import java.nio.file.Paths;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiNative;
import me.nithanim.fragmentationstatistics.natives.linux.StatStruct;

public class FibmapMain {

    public static void main(String[] args) throws Exception {
        LinuxApi la = new LinuxApiNative();
        Path path = Paths.get("/home/nithanim/FTB_Launcher.jar");
        //Path path = Paths.get("/media/nithanim/My Book/test/file.test");
        try (File f = File.open(la, path)) {
            int blocksize = la.getBlocksize(f.getFd());

            try (StatStruct st = StatStruct.allocate()) {
                la.fstat(f.getFd(), st);

                int blockCount = (int) ((st.getSize() + blocksize - 1) / blocksize);

                long filesize = st.getSize();
                System.out.format("File: %s Size: %d Blocks: %s Blocksize: %s\n", path, filesize, Long.toUnsignedString(blockCount), Long.toUnsignedString(blocksize));

                int currentBlockValue = ~0;

                int fd = f.getFd();
                for (int i = 0; Integer.compareUnsigned(i, blockCount) < 0; i++) {
                    int nextBlockValue = la.fibmap(fd, i);

                    if (currentBlockValue + 1 != nextBlockValue) { // beginning of new extent
                        if (currentBlockValue != ~0) { // don't save dummy beginning

                        }
                    }

                    System.out.format("%3d %10s\n", i, Long.toUnsignedString(nextBlockValue));
                }
            }
        }
    }
}
