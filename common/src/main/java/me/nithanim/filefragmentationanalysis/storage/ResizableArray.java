package me.nithanim.filefragmentationanalysis.storage;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Basically the same as {@link ResizableIntArray} but for objects.
 *
 * @param <T>
 */
public class ResizableArray<T> implements Iterable<T> {
    private static final int DEFAULT_PARENT_LENGTH = 100;
    private static final int DEFAULT_CHILD_LENGTH = 100;

    private Object parent[][] = new Object[DEFAULT_PARENT_LENGTH][];

    private int currentParentIndex;
    private int currentChildIndex;

    private long size;

    public ResizableArray() {
        this(DEFAULT_CHILD_LENGTH);
    }

    public ResizableArray(int initialSize) {
        parent[0] = new Object[initialSize];
    }

    public long getSize() {
        return size;
    }

    public void add(T o) {
        ensureWriteable();
        parent[currentParentIndex][currentChildIndex++] = o;
        size++;
    }

    private void ensureWriteable() {
        if (currentChildIndex > parent[currentParentIndex].length - 1) {
            currentParentIndex++;
            currentChildIndex = 0;
            ensureParentSpace();

            int nextLen;
            if ((currentParentIndex & 0b1) == 0) {
                nextLen = parent[currentParentIndex - 1].length * 2;
            } else {
                nextLen = parent[currentParentIndex - 1].length;
            }
            parent[currentParentIndex] = new Object[nextLen];
        }
    }

    private void ensureParentSpace() {
        if (currentParentIndex > parent.length - 1) {
            parent = Arrays.copyOf(parent, newSize(parent.length));
        }
    }

    private int newSize(int oldSize) {
        return oldSize + (oldSize >> 1);
    }

    @Override
    public Iterator<T> iterator() {
        return new ObjectIterator();
    }

    public class ObjectIterator implements Iterator<T> {
        private int pIdx;
        private int cIdx;

        @Override
        public boolean hasNext() {
            if (pIdx == currentParentIndex) {
                return cIdx < currentChildIndex;
            } else {
                //if smaller then there must be more and if bigger then it is not possible
                return pIdx < currentParentIndex;
            }
        }

        @Override
        public T next() {
            Object[] p = parent[pIdx];
            Object r = p[cIdx++];

            if (p.length <= cIdx) {
                pIdx++;
                cIdx = 0;
            }
            return (T) r;
        }
    }
}
