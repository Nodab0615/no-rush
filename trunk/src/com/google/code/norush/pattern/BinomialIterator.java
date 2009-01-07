package com.google.code.norush.pattern;

import java.util.Iterator;

/**
 * Allows iteration over all the ways to pick <i>groupSize</i> items out of a
 * total <i>numItems</i> items.
 * 
 * Taken from http://sourceforge.net/projects/corewars8086
 * 
 * @author BS
 */
public class BinomialIterator implements Iterator<int[]> {
    private int[] group;
    private int size, numItems;
    
    public BinomialIterator(int numItems, int groupSize) {
        assert numItems >= groupSize;
        
        group = new int[groupSize];
        size = groupSize;
        this.numItems = numItems;
        for (int i = 1; i < groupSize; i++) {
            group[i] = i;
        }
    }	
    
    public boolean hasNext() {
        return group != null;
    }

    /**
     * Returns the next group in the sequence
     */
    public int[] next() {
        int i;
        int[] copy = new int[size];
        System.arraycopy(group, 0, copy, 0, size);
        for (i = size -1; i >= 0; i--) {
            if ((group[i] < numItems-1)  && (numItems - 1 - (group[i]+1) >= size - i-1)) {
                group[i] ++;
                for (int j = i+1; j < size; j++) {
                    group[j] = group[j-1] +1;
                }
                break;
            }
        }
        if (i == -1) {
            group = null;
        }
        return copy;
    }
    
    /** @see java.util.Iterator#remove() */
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
