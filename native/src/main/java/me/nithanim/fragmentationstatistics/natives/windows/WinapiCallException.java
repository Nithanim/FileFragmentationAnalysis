package me.nithanim.fragmentationstatistics.natives.windows;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;

public class WinapiCallException extends RuntimeException {
    private final String name;

    public WinapiCallException(String name) {
        super("Error calling winapi " + name + ": " + Kernel32.INSTANCE.GetLastError() + " " + Kernel32Util.getLastErrorMessage());
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
