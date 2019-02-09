package me.nithanim.filefragmentationanalysis.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import me.nithanim.filefragmentationanalysis.fragmentation.FragmentationAnalyzationException;
import me.nithanim.filefragmentationanalysis.scanning.PathFragmentationScanner;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.formats.reader.FragStorageFormatReader;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatSelector;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatType;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatWriter;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;

public class CliMain {
    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        CliData cliData = new CliData();
        try {
            ParseResult parseResult = new CommandLine(cliData).parseArgs(args);
            if (!CommandLine.printHelpIfRequested(parseResult)) {
                if (cliData.getOutputFile() == null && !cliData.isUpload()) {
                    System.err.print("Either the result has to be stored in a file or uploaded!");
                    return;
                }

                OutputStream outFileStream = null;
                try {
                    if (cliData.getOutputFile() != null) {
                        outFileStream = Files.newOutputStream(cliData.getOutputFile(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    }

                    Path p = cliData.getInputDirectory();
                    Index index = getIndex(p, cliData);

                    StorageFormatType outputType = cliData.getOutputFileType();
                    if (outputType == null) {
                        outputType = StorageFormatType.FRAG;
                    }
                    boolean outputCompressed = cliData.isOutputFileCompressed();

                    StorageFormatWriter writer = StorageFormatSelector.getWriter(outputType, outputCompressed);
                    if (outFileStream != null) {
                        writer.write(outFileStream, index);
                    }
                    if (cliData.isUpload()) {
                        //TODO upload
                    }
                } finally {
                    if (outFileStream != null) {
                        outFileStream.close();
                    }
                }
            }
        } catch (ParameterException ex) { // command line arguments could not be parsed
            System.err.println(ex.getMessage());
            ex.getCommandLine().usage(System.err);
        }
    }

    private static Index getIndex(Path p, CliData cliData) throws ExecutionException, InterruptedException, IOException {
        if (p.getFileName().toString().endsWith(".ffi")) {
            return loadStorage(p);
        } else {
            System.out.println("Starting scan...");
            try {
                return scanFileTree(cliData, p);
            } finally {
                System.out.println("Done scanning!");
            }
        }
    }

    private static Index scanFileTree(CliData cliData, Path p) throws ExecutionException, InterruptedException {
        Consumer<FragmentationAnalyzationException> onException;
        if (cliData.isNoPrintErrors()) {
            onException = (ex) -> {
            };
        } else {
            onException = (ex) -> System.err.println(ex.getPath() + ": " + ex.getCause().getMessage());
        }

        PathFragmentationScanner.Scan sl = new PathFragmentationScanner().scan(p, onException);

        while (!sl.getFuture().isDone()) {
            Thread.sleep(5000);
            System.out.println(sl.getCurrentPath().get());
        }
        PathFragmentationScanner.ScanResult result = sl.getFuture().get();

        return result.getIndex();
    }

    private static Index loadStorage(Path p) throws IOException {
        try (InputStream in = Files.newInputStream(p)) {
            return new FragStorageFormatReader().read(in);
        }
    }
}
