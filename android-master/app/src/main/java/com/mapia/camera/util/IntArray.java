package com.mapia.camera.util;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class IntArray
{
    private static final int INIT_CAPACITY = 8;
    private int[] mData;
    private int mSize;

    public IntArray() {
        super();
        this.mData = new int[8];
        this.mSize = 0;
    }

    public void add(final int n) {
        if (this.mData.length == this.mSize) {
            final int[] mData = new int[this.mSize + this.mSize];
            System.arraycopy(this.mData, 0, mData, 0, this.mSize);
            this.mData = mData;
        }
        this.mData[this.mSize++] = n;
    }

    public void clear() {
        this.mSize = 0;
        if (this.mData.length != 8) {
            this.mData = new int[8];
        }
    }

    public int[] getInternalArray() {
        return this.mData;
    }

    public int removeLast() {
        --this.mSize;
        return this.mData[this.mSize];
    }

    public int size() {
        return this.mSize;
    }

    public int[] toArray(final int[] array) {
        int[] array2 = null;
        Label_0022: {
            if (array != null) {
                array2 = array;
                if (array.length >= this.mSize) {
                    break Label_0022;
                }
            }
            array2 = new int[this.mSize];
        }
        System.arraycopy(this.mData, 0, array2, 0, this.mSize);
        return array2;
    }
}