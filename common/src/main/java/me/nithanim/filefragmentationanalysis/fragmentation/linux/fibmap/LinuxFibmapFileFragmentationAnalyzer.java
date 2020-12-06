package me.nithanim.filefragmentationanalysis.fragmentation.linux.fibmap;

import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.File;
import me.nithanim.filefragmentationanalysis.fragmentation.linux.common.LinuxSubFileFragmentationAnalyzer;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApi;
import me.nithanim.fragmentationstatistics.natives.linux.LinuxApiPanama;
import me.nithanim.fragmentationstatistics.natives.linux.StatStruct;

public class LinuxFibmapFileFragmentationAnalyzer implements LinuxSubFileFragmentationAnalyzer {
    private final StatStruct st;
    private final LinuxApi la;

    public LinuxFibmapFileFragmentationAnalyzer() {
        this(new LinuxApiPanama());
    }

    public LinuxFibmapFileFragmentationAnalyzer(LinuxApi la) {
        this.la = la;
        this.st = la.allocateStatStruct();
    }

    @Override
    public List<Fragment> analyze(File f, int blockSize) throws Exception {
        la.fstat(f.getFd(), st);
        long nBlocks = (st.getSize() + blockSize - 1) / blockSize;

        long fileSize = st.getSize();
        //System.out.format("File: %s Size: %d Blocks: %s Blocksize: %s\n", f, fileSize, Long.toUnsignedString(nBlocks), Long.toUnsignedString(blockSize));

        BlockMerger em = new BlockMerger(blockSize);
        int fd = f.getFd();
        for (int i = 0; Long.compareUnsigned(i, nBlocks) < 0; i++) {
            long blockValue = la.fibmap(fd, i);

            em.add(blockValue);

            //System.out.format("%3d %10s\n", i, Long.toUnsignedString(blockValue));
        }

        return em.complete();
    }

    @Override
    public void close() throws Exception {
        st.close();
    }
}
