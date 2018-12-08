package me.nithanim.filefragmentationanalysis;

import me.nithanim.filefragmentationanalysis.cli.CliMain;
import me.nithanim.filefragmentationanalysis.gui.GuiMain;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            GuiMain.main(args);
        } else {
            CliMain.main(args);
        }
    }
}
