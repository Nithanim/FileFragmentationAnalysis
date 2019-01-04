package me.nithanim.filefragmentationanalysis.fragmentation.win;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBuffer;

/**
 * Combines muliple extents that form a contiguous space on the disk to a
 * fragment.
 *
 * @see
 */
public class ExtentToFragmentCombiner {
    private final List<Fragment> fragments;

    private final Path p;
    private final int blocksize;

    boolean openFragment = false;
    /**
     * The cluster offset in the file where the current fragment started.
     */
    private long fragmentStartVcn = 0;
    /**
     * The cluster offset on the disk where the current fragment started.
     */
    private long fragmentStartLcn;
    private long fragmentLength;
    /**
     * The predicted next cluster offset in the file. Useful for differentiating
     * on the next invocation if the extent belongs to the current fragment of
     * is a new one.
     */
    private long nextLcnExpected;

    public ExtentToFragmentCombiner(Path p, int blocksize) {
        fragments = new ArrayList<>();
        this.p = p;
        this.blocksize = blocksize;
    }

    public List<Fragment> complete() {
        if (openFragment) {
            closeFragment();
            return complete();
        } else {
            return fragments;
        }
    }

    public void add(long currentVcn, RetrievalPointersBuffer.Extent e) {
        long nextVcn = e.getNextVcn();
        long currentLcn = e.getLcn();

        if (currentLcn == -1) { //virtual fragment, not allocated on disk or compressed (partial allocated)
            //basically ignore because not on disk
            //take all values from previous extent
            return;
        } else if (!openFragment) { //now on allocated extent but previously no fragment (at start or prev was virtual)
            //begin new fragment
            //System.out.println("Extent: Lcn=" + lcn + ", Vcn=" + currVcn + ", NextVcn=" + nextVcn);
            openFragment(currentVcn, currentLcn, nextVcn);
        } else if (nextLcnExpected == currentLcn) { //check if the end pos of prev is same as beginning of current extent
            //so we continue the fragment
            //System.out.println("Extent: Lcn=" + lcn + ", Vcn=" + currVcn + ", NextVcn=" + nextVcn);
            fragmentLength += nextVcn - currentVcn;
        } else {
            //expected lcn is not the same, so a jump on disk; a non-contiguous extent.
            //finish the active fragment and begin a new one
            //System.out.println("Extent: Lcn=" + lcn + ", Vcn=" + currVcn + ", NextVcn=" + nextVcn);
            closeFragment();
            openFragment(currentVcn, currentLcn, nextVcn);
        }
        nextLcnExpected = currentLcn + (nextVcn - currentVcn);
    }

    private void openFragment(long currentVcn, long lcn, long nextVcn) {
        openFragment = true;
        fragmentStartVcn = currentVcn;
        fragmentStartLcn = lcn;
        fragmentLength = nextVcn - currentVcn;
    }

    private void closeFragment() {
        Fragment c = new Fragment(
            fragmentStartVcn * blocksize,
            fragmentStartLcn * blocksize,
            fragmentLength * blocksize
        );
        fragments.add(c);
        openFragment = false;
    }
}
