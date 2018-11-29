package me.nithanim.filefragmentationanalysis.storage;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ResizableIntArrayTest {
    @Test
    public void testAdd() {
        ResizableIntArray a = new ResizableIntArray(2);
        int its = 5200;
        for (int i = 0; i < its; i++) {
            a.add(i);
        }

        ResizableIntArray.IntIterator it = a.iterator();
        for (int i = 0; i < its; i++) {
            assertEquals(true, it.hasNext());
            assertEquals(i, it.next());
        }
        assertEquals(false, it.hasNext());
    }
}
