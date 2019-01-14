package me.nithanim.filefragmentationanalysis.fragmentation.linux.common;

import java.util.ArrayList;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;

/**
 * Similar to
 * {@link me.nithanim.filefragmentationanalysis.fragmentation.win.ExtentToFragmentCombinerExtentToFragmentCombiner}
 * of Windows.
 *
 * @see
 */
public class ExtentToFragmentCombiner {
    private final List<Fragment> fragments;

    private final int blockSize;

    boolean openFragment = false;
    private long fragmentStartLogical = 0;
    private long fragmentStartPhysical;
    private long fragmentLength;
    private long expectedNextPhysical;

    public ExtentToFragmentCombiner(int blockSize) {
        fragments = new ArrayList<>();
        this.blockSize = blockSize;
    }

    public List<Fragment> complete() {
        if (openFragment) {
            closeFragment();
            return complete();
        } else {
            return fragments;
        }
    }

    public void add(long logical, long physical, long length) {
        if (!openFragment) {
            openFragment(logical, physical, length);
        } else if (expectedNextPhysical == physical) {
            fragmentLength += length;
        } else {
            closeFragment();
            openFragment(logical, physical, length);
        }
        expectedNextPhysical = physical + length;
    }

    private void openFragment(long logical, long physical, long length) {
        openFragment = true;
        fragmentStartLogical = logical;
        fragmentStartPhysical = physical;
        fragmentLength = length;
    }

    private void closeFragment() {
        Fragment c = new Fragment(
            fragmentStartLogical,
            fragmentStartPhysical,
            fragmentLength
        );
        fragments.add(c);
        openFragment = false;
    }
}
