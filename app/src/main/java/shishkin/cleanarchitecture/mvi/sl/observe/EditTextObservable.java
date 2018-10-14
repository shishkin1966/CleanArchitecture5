package shishkin.cleanarchitecture.mvi.sl.observe;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;


import shishkin.cleanarchitecture.mvi.common.Debounce;

public class EditTextObservable extends Observable implements TextWatcher {
    private EditText mEditText;
    private long mDelay = 500;
    private Debounce mDebouce;
    private WeakReference<Observer> mObserver;

    public EditTextObservable(Observer observer, @NonNull final EditText view) {
        this(observer, view, 500);
    }

    public EditTextObservable(Observer observer, @NonNull final EditText view, final long delay) {
        mEditText = view;
        mDelay = delay;
        mEditText.addTextChangedListener(this);
        mObserver = new WeakReference<>(observer);
        mDebouce = new Debounce(mDelay) {
            @Override
            public void run() {
                if (mObserver != null && mObserver.get() != null) {
                    mObserver.get().update(EditTextObservable.this, mEditText.getText().toString());
                }
            }
        };
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(final CharSequence s, final int start,
                              final int before, final int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        this.setChanged();
        mDebouce.onEvent();
    }

    public void finish() {
        mDebouce.finish();
    }

    public EditText getView() {
        return mEditText;
    }

}
