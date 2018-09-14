package shishkin.cleanarchitecture.mvi.common.collection;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * IntArrayList holds integers to Objects. It is intended to be more memory efficient
 * than using an List&lt;Integer> to store Integers objects, because it avoids
 * auto-boxing.
 */
@SuppressWarnings("unused")
public class IntArrayList implements Parcelable {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int MIN_CAPACITY_INCREMENT = 12;

    private int mSize;
    private int[] mItems;

    /**
     * Constructs a new instance of {@code IntArrayList}.
     */
    public IntArrayList() {
        mSize = 0;
        mItems = new int[DEFAULT_CAPACITY];
    }

    /* package */ IntArrayList(final Parcel in) {
        mSize = in.readInt();
        final int length = in.readInt();
        mItems = new int[length];
        in.readIntArray(mItems);
    }

    /**
     * Adds the specified number at the end of this {@code IntArrayList}.
     *
     * @param item the number to add.
     * @return always true
     */
    public boolean add(final int item) {
        int[] a = mItems;
        final int s = mSize;
        if (s == a.length) {
            int[] newArray = new int[newCapacity(s)];
            System.arraycopy(a, 0, newArray, 0, s);
            mItems = a = newArray;
        }
        a[s] = item;
        mSize = s + 1;
        return true;
    }

    /**
     * Returns the element at the specified location in this list.
     *
     * @param index the index of the element to return.
     * @return the element at the specified index.
     * @throws IndexOutOfBoundsException if {@code location < 0 || location >= size()}
     */
    @SuppressWarnings("all")
    public int get(final int index) {
        final int size = mSize;
        if (index >= size) {
            throwIndexOutOfBoundsException(index, size);
        }
        return mItems[index];
    }

    /**
     * Removes the element at the specified location from this list.
     *
     * @param index the index of the object to remove.
     * @return the removed element.
     * @throws IndexOutOfBoundsException when {@code location < 0 || location >= size()}
     */
    @SuppressWarnings("all")
    public int remove(final int index) {
        final int[] a = mItems;
        int s = mSize;
        if (index >= s) {
            throwIndexOutOfBoundsException(index, s);
        }

        final int result = a[index];
        System.arraycopy(a, index + 1, a, index, --s - index);
        a[s] = 0;  // Prevent memory leak
        mSize = s;
        return result;
    }

    /**
     * Searches this {@code IntArrayList} for the specified number.
     *
     * @param item the number to search for.
     * @return {@code true} if {@code item} is an element of this
     * {@code IntArrayList}, {@code false} otherwise
     */
    public boolean contains(final int item) {
        final int s = mSize;
        int[] a = mItems;

        boolean contains = false;
        for (int i = 0; i < s; i++) {
            if (a[i] == item) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    /**
     * Removes all elements from this {@code IntArrayList}, leaving it empty.
     *
     * @see #isEmpty()
     * @see #size()
     */
    public void clear() {
        if (mSize != 0) {
            mSize = 0;
        }
    }

    /**
     * Returns the number of elements in this {@code IntArrayList}.
     */
    public int size() {
        return mSize;
    }

    /**
     * Returns if this {@code IntArrayList} contains no elements. This implementation
     * tests, whether {@code size} returns 0.
     *
     * @return {@code true} if this {@code IntArrayList} has no elements, {@code false}
     * otherwise.
     * @see #size
     */
    public boolean isEmpty() {
        return (mSize == 0);
    }

    /**
     * Searches this list for the specified number and returns the index of the
     * first occurrence.
     *
     * @param item) the number to search for.
     * @return the index of the first occurrence of the number, or -1 if it was
     * not found.
     */
    public int indexOf(final int item) {
        final int[] a = mItems;
        final int s = mSize;
        for (int i = 0; i < s; i++) {
            if (item == a[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a new array containing all elements contained in this
     * {@code IntArrayList}.
     *
     * @return an array of the elements from this {@code IntArrayList}
     */
    public int[] toArray() {
        final int s = mSize;
        final int[] result = new int[s];
        System.arraycopy(mItems, 0, result, 0, s);
        return result;
    }

    @Override
    public int hashCode() {
        final int[] a = mItems;
        int hashCode = 1;
        for (int i = 0, s = mSize; i < s; i++) {
            final int e = a[i];
            hashCode = 31 * hashCode + e;
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(IntArrayList.class.isInstance(o))) {
            return false;
        }

        final IntArrayList that = (IntArrayList) o;
        final int s = mSize;
        if (that.size() != s) {
            return false;
        }

        final int[] a = mItems;
        for (int i = 0; i < s; i++) {
            int eThis = a[i];
            int eThat = that.get(i);
            if (eThis != eThat) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mSize);
        dest.writeInt(mItems.length);
        dest.writeIntArray(mItems);
    }

    private static IndexOutOfBoundsException throwIndexOutOfBoundsException(final int index, final int size) {
        throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size);
    }

    private static int newCapacity(final int currentCapacity) {
        final int increment = (currentCapacity < (MIN_CAPACITY_INCREMENT / 2) ? MIN_CAPACITY_INCREMENT : currentCapacity >> 1);
        return currentCapacity + increment;
    }

    public static final Creator<IntArrayList> CREATOR = new Creator<IntArrayList>() {

        @Override
        public IntArrayList createFromParcel(final Parcel in) {
            return new IntArrayList(in);
        }

        @Override
        public IntArrayList[] newArray(final int size) {
            return new IntArrayList[size];
        }

    };

}
