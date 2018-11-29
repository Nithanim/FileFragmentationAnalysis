package me.nithanim.filefragmentationanalysis.fragmentation.linux.fibmap;

import java.util.ArrayList;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;

public class BlockMerger {
    private final long blockSize;

    private long currentBlockIndex = 0;
    private long currentBlockValue;
    private long startBlockIndex;
    private long startBlockValue;

    private final List<Fragment> fragments = new ArrayList<>();

    public BlockMerger(long blockSize) {
        this.blockSize = blockSize;
    }

    public void add(long value) {
        if (currentBlockIndex == 0) {
            startBlockIndex = 0;
            startBlockValue = value;
            currentBlockValue = value;
        } else {
            if (currentBlockValue + 1 != value) { // contiguous block broken; jump
                long endBlockIndex = currentBlockIndex - 1;
                fragments.add(new Fragment(
                    startBlockIndex * blockSize,
                    startBlockValue * blockSize,
                    (endBlockIndex - startBlockIndex + 1) * blockSize
                ));

                startBlockIndex = currentBlockIndex;
                startBlockValue = value;
                currentBlockValue = value;
            } else {
                currentBlockValue = value;
            }
        }

        currentBlockIndex++;
    }

    public List<Fragment> complete() {
        if (currentBlockIndex != 0) {
            fragments.add(new Fragment(
                startBlockIndex * blockSize,
                startBlockValue * blockSize,
                (currentBlockIndex - startBlockIndex) * blockSize
            ));
        }
        return fragments;
    }
}
