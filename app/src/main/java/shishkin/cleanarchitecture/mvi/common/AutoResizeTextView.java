package shishkin.cleanarchitecture.mvi.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * http://stackoverflow.com/questions/16017165/auto-fit-textview-for-android/21851239
 */
public class AutoResizeTextView extends TextView {

    public AutoResizeTextView(Context context) {
        super(context);
        initialize();
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private interface SizeTester {
        /**
         * @param suggestedSize  Size of text to be tested
         * @param availableSpace available space in which text must fit
         * @return an integer < 0 if after applying {@code suggestedSize} to
         * text, it takes less space than {@code availableSpace}, > 0
         * otherwise
         */
        int onTestSize(int suggestedSize, RectF availableSpace);
    }

    private RectF mTextRect = new RectF();

    private RectF mAvailableSpaceRect;

    private SparseIntArray mTextCachedSizes;

    private TextPaint mPaint;

    private float mMaxTextSize;

    private float mSpacingMult = 1.0f;

    private float mSpacingAdd = 0.0f;

    private float mMinTextSize = 12;

    private int mWidthLimit;

    private static final int NO_LINE_LIMIT = -1;
    private int mMaxLines;

    private boolean mEnableSizeCache = true;
    private boolean mInitializedDimens;

    private void initialize() {
        mPaint = new TextPaint(getPaint());
        mMaxTextSize = getTextSize();
        mAvailableSpaceRect = new RectF();
        mTextCachedSizes = new SparseIntArray();
        if (mMaxLines == 0) {
            // no value was assigned during construction
            mMaxLines = NO_LINE_LIMIT;
        }
    }

    @Override
    public void setTextSize(float size) {
        mMaxTextSize = size;
        mTextCachedSizes.clear();
        adjustTextSize();
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
        mMaxLines = maxlines;
        adjustTextSize();
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        mMaxLines = 1;
        adjustTextSize();
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        super.setSingleLine(singleLine);
        if (singleLine) {
            mMaxLines = 1;
        } else {
            mMaxLines = NO_LINE_LIMIT;
        }
        adjustTextSize();
    }

    @Override
    public void setLines(int lines) {
        super.setLines(lines);
        mMaxLines = lines;
        adjustTextSize();
    }

    @Override
    public void setTextSize(int unit, float size) {
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        mMaxTextSize = TypedValue.applyDimension(unit, size,
                r.getDisplayMetrics());
        mTextCachedSizes.clear();
        adjustTextSize();
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        mSpacingMult = mult;
        mSpacingAdd = add;
    }

    /**
     * Set the lower text size limit and invalidate the view
     *
     * @param minTextSize
     */
    public void setMinTextSize(float minTextSize) {
        mMinTextSize = minTextSize;
        adjustTextSize();
    }

    private void adjustTextSize() {
        if (!mInitializedDimens) {
            return;
        }
        int startSize = (int) mMinTextSize;
        int heightLimit = getMeasuredHeight() - getCompoundPaddingBottom()
                - getCompoundPaddingTop();
        mWidthLimit = getMeasuredWidth() - getCompoundPaddingLeft()
                - getCompoundPaddingRight();
        mAvailableSpaceRect.right = mWidthLimit;
        mAvailableSpaceRect.bottom = heightLimit;
        super.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                efficientTextSizeSearch(startSize, (int) mMaxTextSize,
                        mSizeTester, mAvailableSpaceRect));
    }

    private final SizeTester mSizeTester = new SizeTester() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public int onTestSize(int suggestedSize, RectF availableSPace) {
            mPaint.setTextSize(suggestedSize);
            String text = getTransformedText();
            boolean singleline = getMaxLines() == 1;
            if (singleline) {
                mTextRect.bottom = mPaint.getFontSpacing();
                mTextRect.right = mPaint.measureText(text);
            } else {
                StaticLayout layout = new StaticLayout(text, mPaint,
                        mWidthLimit, Alignment.ALIGN_NORMAL, mSpacingMult,
                        mSpacingAdd, true);
                // return early if we have more lines
                if (getMaxLines() != NO_LINE_LIMIT
                        && layout.getLineCount() > getMaxLines()) {
                    return 1;
                }
                mTextRect.bottom = layout.getHeight();
                int maxWidth = -1;
                for (int i = 0; i < layout.getLineCount(); i++) {
                    if (maxWidth < layout.getLineWidth(i)) {
                        maxWidth = (int) layout.getLineWidth(i);
                    }
                }
                mTextRect.right = maxWidth;
            }

            mTextRect.offsetTo(0, 0);
            if (availableSPace.contains(mTextRect)) {
                // may be too small, don't worry we will find the best match
                return -1;
            } else {
                // too big
                return 1;
            }
        }
    };

    /**
     * Enables or disables size caching, enabling it will improve performance
     * where you are animating a value inside TextView. This stores the font
     * size against getText().length() Be careful though while enabling it as 0
     * takes more space than 1 on some fonts and so on.
     *
     * @param enable enable font size caching
     */
    public void enableSizeCache(boolean enable) {
        mEnableSizeCache = enable;
        mTextCachedSizes.clear();
        adjustTextSize();
    }

    private int efficientTextSizeSearch(int start, int end,
                                        SizeTester sizeTester, RectF availableSpace) {
        if (!mEnableSizeCache) {
            return binarySearch(start, end, sizeTester, availableSpace);
        }
        int key = getText().toString().length();
        int size = mTextCachedSizes.get(key);
        if (size != 0) {
            return size;
        }
        size = binarySearch(start, end, sizeTester, availableSpace);
        mTextCachedSizes.put(key, size);
        return size;
    }

    private static int binarySearch(int start, int end, SizeTester sizeTester,
                                    RectF availableSpace) {
        int lastBest = start;
        int lo = start;
        int hi = end - 1;
        int mid;
        while (lo <= hi) {
            mid = (lo + hi) >>> 1;
            int midValCmp = sizeTester.onTestSize(mid, availableSpace);
            if (midValCmp < 0) {
                lastBest = lo;
                lo = mid + 1;
            } else if (midValCmp > 0) {
                hi = mid - 1;
                lastBest = hi;
            } else {
                return mid;
            }
        }
        // make sure to return last best
        // this is what should always be returned
        return lastBest;

    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start,
                                 final int before, final int after) {
        super.onTextChanged(text, start, before, after);
        adjustTextSize();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth,
                                 int oldheight) {
        mInitializedDimens = true;
        mTextCachedSizes.clear();
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if (width != oldwidth || height != oldheight) {
            adjustTextSize();
        }
    }

    private String getTransformedText() {
        CharSequence text = getText();
        if (text != null) {
            TransformationMethod transformationMethod = getTransformationMethod();
            if (transformationMethod != null) {
                text = transformationMethod.getTransformation(text, this);
            }
        }
        return text == null ? null : text.toString();
    }
}