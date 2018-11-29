package me.nithanim.fragmentationstatistics.natives;

import lombok.Getter;

@Getter
public class NativeCallException extends RuntimeException {
    private final String function;
    private final int errorCode;

    public NativeCallException(String function, long returnCode, int errorCode) {
        super("Exception in native call: function " + function + " returned " + returnCode + " with error code " + errorCode);
        this.function = function;
        this.errorCode = errorCode;
    }

    public NativeCallException(String function, long returnCode, int errorCode, String errorString) {
        super("Exception in native call: function " + function + " returned " + returnCode + " with error code " + errorCode + ": " + errorString);
        this.function = function;
        this.errorCode = errorCode;
    }
}
