package me.nithanim.filefragmentationanalysis.fragmentation;

import java.nio.file.Path;

public class FragmentationAnalyzationException extends RuntimeException {
    private final Path path;

    public FragmentationAnalyzationException(Path p, Throwable cause) {
        super("Error analyzing " + p, cause);
        this.path = p;
    }

    public FragmentationAnalyzationException(Path p, String message, Throwable cause) {
        super(message, cause);
        this.path = p;
    }

    public Path getPath() {
        return path;
    }
}
