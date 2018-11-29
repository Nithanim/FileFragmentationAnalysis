package me.nithanim.fragmentationstatistics.natives.linux;

public class RootRequiredException extends RuntimeException {
    public RootRequiredException() {
        super();
    }

    public RootRequiredException(String msg) {
        super(msg);
    }
}
