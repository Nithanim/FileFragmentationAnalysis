package me.nithanim.filefragmentationanalysis.cli;

import java.nio.file.Path;
import lombok.Getter;
import lombok.ToString;
import picocli.CommandLine.Option;

@Getter
@ToString
public class CliData {
    @Option(names = {"-i", "--in"}, description = "The directory to scan for fragment information or a saved scan", required = true)
    private Path inputDirectory;

    @Option(names = {"-o", "--out"}, description = "The file to save the sanning result to")
    private Path outputFile;

    @Option(names = {"-u", "--upload"}, description = "Upload the result")
    private boolean upload;
    
    @Option(names = {"--no-print-errors"}, description = "Do not print errors")
    private boolean noPrintErrors;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Display this help message")
    boolean usageHelpRequested;
}
