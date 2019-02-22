package me.nithanim.filefragmentationanalysis.fragmentation;

import java.nio.file.Paths;

public class NativeToolboxMain {
    public static void main(String[] args) {
        NativeToolbox nt = NativeToolbox.create();
        System.out.println(nt.getFileSystemUtil().getFileSystemInformation(Paths.get("/")));
    }
}
