package me.nithanim.filefragmentationanalysis.fragmentation.linux.fibmap;

import java.util.Arrays;
import java.util.List;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

public class ExtentMergerTest {
    @Test
    public void zeroLengthFile() {
        BlockMerger em = new BlockMerger(1);
        Assert.assertEquals(0, em.complete().size());
    }

    @Test
    public void singleBlockFile() {
        BlockMerger em = new BlockMerger(5);
        em.add(3);

        List<Fragment> expected = Arrays.asList(new Fragment(0, 3 * 5, 1 * 5)
        );
        MatcherAssert.assertThat(em.complete(), CoreMatchers.is(expected));
    }

    @Test
    public void singleBlocksize() {
        BlockMerger em = new BlockMerger(1);
        int v;
        em.add(v = 5);
        em.add(++v);
        em.add(++v);
        em.add(++v);

        em.add(v = 20);
        em.add(++v);
        em.add(++v);

        em.add(v = 30);

        List<Fragment> expected = Arrays.asList(new Fragment(0, 5, 4),
            new Fragment(4, 20, 3),
            new Fragment(7, 30, 1)
        );

        MatcherAssert.assertThat(em.complete(), CoreMatchers.is(expected));
    }

    @Test
    public void biggerBlocksize() {
        BlockMerger em = new BlockMerger(5);
        int v;
        em.add(v = 5);
        em.add(++v);
        em.add(++v);
        em.add(++v);

        em.add(v = 20);
        em.add(++v);
        em.add(++v);

        em.add(v = 30);

        List<Fragment> expected = Arrays.asList(new Fragment(0 * 5, 5 * 5, 4 * 5),
            new Fragment(4 * 5, 20 * 5, 3 * 5),
            new Fragment(7 * 5, 30 * 5, 1 * 5)
        );

        MatcherAssert.assertThat(em.complete(), CoreMatchers.is(expected));
    }
}
