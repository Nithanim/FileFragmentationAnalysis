package me.nithanim.fragmentationstatistics.natives;

import java.util.concurrent.atomic.AtomicBoolean;

public class NativeLoader {
    private static final AtomicBoolean loaded = new AtomicBoolean(false);

    public static void loadLibrary() {
        if (!loaded.getAndSet(true)) {
            NarSystem.loadLibrary();
        }
    }
}
