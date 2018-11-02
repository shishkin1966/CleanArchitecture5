package shishkin.cleanarchitecture.mvi.common.collection;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * LongArrayList holds longs to Objects. It is intended to be more memory efficient
 * than using an List&lt;Long> to store Long objects, because it avoids
 * auto-boxing.
 */
@SuppressWarnings("unused")
public class LongArrayList implements Parcelable {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int MIN_CAPACITY_INCREMENT = 12;

    private int mSize;
    private long[] mItems;

    /**
     * Constructs a new instance of {@code LongArrayList}.
     */
    public LongArrayList() {
        mSize = 0;
        mItems = new long[DEFAULT_CAPACITY];
    }

    public LongArrayList(@NonNull final LongArrayList longArrayList) {
        mSize = longArrayList.mSize;
        mItems = new long[newCapacity(mSize)];
        final long[] src = longArrayList.mItems;
        System.arraycopy(src, 0, mItems, 0, mSize);
    }

    /* package */ LongArrayList(final Parcel in) {
        mSize = in.readInt();
        final int length = in.readInt();
        mItems = new long[length];
        in.readLongArray(mItems);
    }

    /**
     * Adds the specified number at the end of this {@code LongArrayList}.
     *
     * @param item the number to add.
     * @return always true
     */
    public boolean add(final long item) {
        long[] a = mItems;
        final int s = mSize;
        if (s == a.length) {
            long[] newArray = new long[newCapacity(s)];
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
    public long get(final int index) {
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
    public long remove(final int index) {
        final long[] a = mItems;
        int s = mSize;
        if (index >= s) {
            throwIndexOutOfBoundsException(index, s);
        }

        final long result = a[index];
        System.arraycopy(a, index + 1, a, index, --s - index);
        a[s] = 0;  // Prevent memory leak
        mSize = s;
        return result;
    }

    /**
     * Searches this {@code LongArrayList} for the specified number.
     *
     * @param item the number to search for.
     * @return {@code true} if {@code item} is an element of this
     * {@code LongArrayList}, {@code false} otherwise
     */
    public boolean contains(final long item) {
        final int s = mSize;
        long[] a = mItems;

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
     * Removes all elements from this {@code LongArrayList}, leaving it empty.
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
     * Returns the number of elements in this {@code LongArrayList}.
     */
    public int size() {
        return mSize;
    }

    /**
     * Returns if this {@code LongArrayList} contains no elements. This implementation
     * tests, whether {@code size} returns 0.
     *
     * @return {@code true} if this {@code LongArrayList} has no elements, {@code false}
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
     * @param item the number to search for.
     * @return the index of the first occurrence of the number, or -1 if it was
     * not found.
     */
    public int indexOf(final long item) {
        final long[] a = mItems;
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
     * {@code LongArrayList}.
     *
     * @return an array of the elements from this {@code LongArrayList}
     */
    public long[] toArray() {
        final int s = mSize;
        final long[] result = new long[s];
        System.arraycopy(mItems, 0, result, 0, s);
        return result;
    }

    @Override
    public int hashCode() {
        final long[] a = mItems;
        int hashCode = 1;
        for (int i = 0, s = mSize; i < s; i++) {
            final long e = a[i];
            hashCode = 31 * hashCode + (int) (e ^ (e >>> 32));
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(LongArrayList.class.isInstance(o))) {
            return false;
        }

        final LongArrayList that = (LongArrayList) o;
        final int s = mSize;
        if (that.size() != s) {
            return false;
        }

        final long[] a = mItems;
        for (int i = 0; i < s; i++) {
            long eThis = a[i];
            long eThat = that.get(i);
            if (eThis != eThat) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        final int size = mSize;
        final long[] items = mItems;

        final StringBuilder sb = new StringBuilder();
        sb.append("LongArrayList [ ");
        for (int i = 0; i < size; i++) {
            sb.append(items[i]);
            if (i < size - 1) {
                sb.append(", ");
            } else {
                sb.append(" ]");
            }
        }
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mSize);
        dest.writeInt(mItems.length);
        dest.writeLongArray(mItems);
    }

    private static IndexOutOfBoundsException throwIndexOutOfBoundsException(final int index, final int size) {
        throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size);
    }

    private static int newCapacity(final int currentCapacity) {
        final int increment = (currentCapacity < (MIN_CAPACITY_INCREMENT / 2) ? MIN_CAPACITY_INCREMENT : currentCapacity >> 1);
        return currentCapacity + increment;
    }

    public static final Creator<LongArrayList> CREATOR = new Creator<LongArrayList>() {

        @Override
        public LongArrayList createFromParcel(final Parcel in) {
            return new LongArrayList(in);
        }

        @Override
        public LongArrayList[] newArray(final int size) {
            return new LongArrayList[size];
        }

    };

}
