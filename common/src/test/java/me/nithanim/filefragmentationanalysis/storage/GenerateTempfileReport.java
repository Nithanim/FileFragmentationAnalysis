package me.nithanim.filefragmentationanalysis.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.fragmentation.NativeToolbox;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileFragmentationAnalyzer;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.FileReport;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.FragStorageFormatWriter;
import me.nithanim.fragmentationstatistics.natives.FileSystemUtil;

public class GenerateTempfileReport {
    public static void main(String[] args) throws IOException {
        Path p = Files.createTempFile("fragtest", "fragtest");
        Files.write(p, new byte[1024 * 1024 * 5], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        NativeToolbox nt = NativeToolbox.create();

        FileFragmentationAnalyzer ffa = nt.getFileFragmentationAnalyzer();
        List<Fragment> fragments = ffa.analyze(p);
        
        FileSystemUtil.FileSystemInformation fsi = nt.getFileSystemUtil().getFileSystemInformation(p);

        Index idx = new Index(p.toString(), nt.getFileSystemUtil().getOperatingSystem(), fsi);
        idx.add(FileReport.of(
            p,
            fragments,
            Files.size(p),
            FileType.FLV,
            LocalDate.now().minusWeeks(4),
            LocalDate.now().minusWeeks(2),
            LocalDate.now().minusWeeks(1),
            LocalDate.now()
        ));

        try (OutputStream out = Files.newOutputStream(Paths.get(System.getProperty("java.io.tmpdir")).resolve("fragtempfile"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            new FragStorageFormatWriter().write(out, idx);
        }
    }
}
