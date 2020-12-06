package me.nithanim.filefragmentationanalysis.fragmentation.linux;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import lombok.SneakyThrows;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.StatStruct;

/**
 * Workaround for inaccessible device id
 */
interface LinuxDeviceGetter extends AutoCloseable {
    long getDev(Path p, BasicFileAttributes bfa);

    public static LinuxDeviceGetter newInstance(LinuxApi linuxApi) {
        try {
            //Try if it works before using it.
            Files.getAttribute(Paths.get("."), "unix:dev");
            return new LinuxDeviceGetterAttributes();
        } catch (Exception ex2) {
            //Last alternative is doing all the work and calling native "stat" and get the infos (again)
            return new LinuxDeviceGetterNative(linuxApi);
        }
    }

    static class LinuxDeviceGetterNative implements LinuxDeviceGetter {
        private final StatStruct ss;
        private final LinuxApi la;

        public LinuxDeviceGetterNative(LinuxApi la) {
            this.ss = la.allocateStatStruct();
            this.la = la;
        }

        @Override
        @SneakyThrows
        public long getDev(Path p, BasicFileAttributes bfa) {
            la.stat(p, ss);
            return ss.getDev();
        }

        @Override
        public void close() throws Exception {
            ss.close();
        }
    }

    static class LinuxDeviceGetterHack extends LinuxDeviceGetterAdapter {
        private final Method unwrapMethod;
        private final Method getDevMethod;

        public LinuxDeviceGetterHack(Method unwrapMethod, Method getDevMethod) {
            this.unwrapMethod = unwrapMethod;
            this.getDevMethod = getDevMethod;
        }

        @Override
        @SneakyThrows
        public long getDev(Path p, BasicFileAttributes bfa) {
            Object attrs = unwrapMethod.invoke(bfa);
            return (long) getDevMethod.invoke(attrs);
        }
    }

    static class LinuxDeviceGetterAttributes extends LinuxDeviceGetterAdapter {
        @Override
        @SneakyThrows
        public long getDev(Path p, BasicFileAttributes bfa) {
            return (long) Files.getAttribute(p, "unix:dev");
        }
    }

    static abstract class LinuxDeviceGetterAdapter implements LinuxDeviceGetter {
        @Override
        public void close() throws Exception {
        }
    }
}
