package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.content.Context;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;


import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView that allow to handle {@link #dispatchTouchEvent(MotionEvent)} callbacks.
 */
@MainThread
public class ObservableRecyclerView extends RecyclerView {

    public interface DispatchTouchEventListener {

        boolean dispatchTouchEvent(@NonNull final ObservableRecyclerView rv,
                                   @NonNull final MotionEvent e);

    }

    public interface OnMeasureListener {

        void onMeasured(@NonNull final ObservableRecyclerView rv);

    }

    @NonNull
    private final List<OnMeasureListener> mOnMeasureListeners;

    @NonNull
    private final List<DispatchTouchEventListener> mDispatchTouchEventListeners;

    public ObservableRecyclerView(@NonNull final Context context) {
        this(context, null);
    }

    public ObservableRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attrs,
                                  final int defStyle) {
        super(context, attrs, defStyle);
        mDispatchTouchEventListeners = new ArrayList<>();
        mOnMeasureListeners = new ArrayList<>();
    }

    public void addOnMeasureListener(@NonNull final OnMeasureListener listener) {
        if (!mOnMeasureListeners.contains(listener)) {
            mOnMeasureListeners.add(listener);
        }
    }

    public void removeOnMeasureListener(@NonNull final OnMeasureListener listener) {
        mOnMeasureListeners.remove(listener);
    }

    public void addDispatchTouchEventListener(@NonNull final DispatchTouchEventListener listener) {
        if (!mDispatchTouchEventListeners.contains(listener)) {
            mDispatchTouchEventListeners.add(listener);
        }
    }

    public void removeDispatchTouchEventListener(@NonNull final DispatchTouchEventListener listener) {
        mDispatchTouchEventListeners.remove(listener);
    }

    @Override
    protected void onMeasure(final int widthSpec, final int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        for (final OnMeasureListener listener : mOnMeasureListeners) {
            listener.onMeasured(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull final MotionEvent ev) {
        for (final DispatchTouchEventListener listener : mDispatchTouchEventListeners) {
            if (listener.dispatchTouchEvent(this, ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
