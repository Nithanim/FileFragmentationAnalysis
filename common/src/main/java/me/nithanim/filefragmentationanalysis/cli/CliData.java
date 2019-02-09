package me.nithanim.filefragmentationanalysis.cli;

import java.nio.file.Path;
import lombok.Getter;
import lombok.ToString;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatType;
import picocli.CommandLine.Option;

@Getter
@ToString
public class CliData {
    @Option(names = {"-i", "--in"}, description = "The directory to scan for fragment information or a saved scan", required = true, paramLabel = "FILE")
    private Path inputDirectory;

    @Option(names = {"-o", "--out"}, description = "The file to save the scanning result to", paramLabel = "FILE")
    private Path outputFile;

    @Option(names = {"-ot", "--out-type"}, description = "The file format of the saved scanning result. Eg. CSV", paramLabel = "TYPE")
    private StorageFormatType outputFileType;

    @Option(names = {"-oc", "--out-compressed"}, description = "Compresses the output file with gzip")
    private boolean outputFileCompressed = false;

    @Option(names = {"-u", "--upload"}, description = "Upload the result")
    private boolean upload;

    @Option(names = {"--no-print-errors"}, description = "Do not print errors")
    private boolean noPrintErrors;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Display this help message")
    boolean usageHelpRequested;
}
