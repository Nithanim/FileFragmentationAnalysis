package me.nithanim.fragmentationstatistics.natives.linux;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) throws Throwable {
        LinuxApiPanama a = new LinuxApiPanama();
        Path path = Paths.get("/home/nithanim/");
        int fd = a.openFileForReading(path);
        System.out.println("Blocksize: " + a.getBlocksize(fd));
        a.closeFile(fd);

        System.out.println("FS magic: " + Long.toHexString(a.getFilesystemType(path)));

        try (StatStruct statStruct = new StatStruct()) {
            a.stat(path, statStruct);
            System.out.println("Stat:");
            System.out.println("    Dev: " + statStruct.getDev());
            System.out.println("    Inode: " + statStruct.getInodeNumber());
            System.out.println("    nHardlinks: " + statStruct.getNumberHardlinks());
            System.out.println("    Mode: " + Integer.toOctalString(statStruct.getMode()));
            System.out.println("    Uid: " + statStruct.getUid());
            System.out.println("    Gid: " + statStruct.getGid());
            System.out.println("    MTime: " + statStruct.getMTime());
        }

    }
}
