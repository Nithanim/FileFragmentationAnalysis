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
import me.nithanim.filefragmentationanalysis.storage.StorageFormatReader;
import me.nithanim.filefragmentationanalysis.storage.StorageFormatWriter;
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
                    System.out.println("Starting scan...");
                    Index index = getIndex(p, cliData);
                    System.out.println("Done scanning!");

                    StorageFormatWriter writer = new StorageFormatWriter();
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
            try (InputStream in = Files.newInputStream(p)) {
                return new StorageFormatReader().read(in);
            }
        } else {
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
    }
}
