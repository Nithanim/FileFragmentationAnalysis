package me.nithanim.filefragmentationanalysis.fragmentation.linux;

import java.lang.reflect.Method;
import java.nio.file.Path;
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
            //Since the needed data is already present but hidden, we try to get access to it.
            //For 9+ it needs the "--add-opens java.base/java.lang=ALL-UNNAMED" jvm arg
            Class<?> uasfb = LinuxFileFragmentationAnalizer.class.getClassLoader().loadClass("sun.nio.fs.UnixFileAttributes$UnixAsBasicFileAttributes");
            Method unwrapMethod = uasfb.getDeclaredMethod("unwrap");
            unwrapMethod.setAccessible(true);

            Class<?> ufs = LinuxFileFragmentationAnalizer.class.getClassLoader().loadClass("sun.nio.fs.UnixFileAttributes");
            Method getDevMethod = ufs.getDeclaredMethod("dev");
            getDevMethod.setAccessible(true);

            return new LinuxDeviceGetterHack(unwrapMethod, getDevMethod);

        } catch (Exception ex) {
            //Alternative is calling native "stat" and get the infos (again)
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
        public long getDev(Path p, BasicFileAttributes bfa) {
            la.stat(p, ss);
            return ss.getDev();
        }

        @Override
        public void close() throws Exception {
            ss.close();
        }
    }

    static class LinuxDeviceGetterHack implements LinuxDeviceGetter {
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

        @Override
        public void close() throws Exception {
        }
    }
}
