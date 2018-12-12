# FileFragmentationAnalysis


Currently Windows and Linux are supported.

This project uses JavaFX. To run it, either Java 8 with JavaFX or Java 11 or higher are required.

## Building

For compilation either [Java 8 with JavaFX](https://www.azul.com/downloads/zulu/zulufx/) or [Java 8](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot) (with or without JavaFX) plus [Java 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot) are required. 
This project includes native code programmed in C that has to be compiled with gcc. To compile this code on Windows [MinGW](https://master.dl.sourceforge.net/project/mingw-w64/Toolchains%20targetting%20Win64/Personal%20Builds/mingw-builds/8.1.0/threads-posix/seh/x86_64-8.1.0-release-posix-seh-rt_v6-rev0.7z) is used.

To manage the C compilation the [nar-maven-plugin](https://github.com/maven-nar/nar-maven-plugin) is utilized. However, it only works with Java 8 and therefore the native module must always be compiled with it.

For native compilation on Windows, the included build-scripts assume that MinGW is extracted into `mingw64` folder. On Linux they assume that gcc is installed and available on the PATH.
For Java compilation, the Java 8 JDK is expected in the `jdk8` folder and the Java 11 JDK in `jdk11`.

Run the `build-system_jdk8` script to use the system install of the Java 8 JDK. Alternatively, use `build-local_jdk8fx` if the locally extracted JDK8 includes JavaFX or `build-local_jdks` to utilize the local JDK 8 and 11.


To execute the compiled program use either `run-system`, `run-local_java8fx` or `run-local_java11` based on your compilation choice above.
