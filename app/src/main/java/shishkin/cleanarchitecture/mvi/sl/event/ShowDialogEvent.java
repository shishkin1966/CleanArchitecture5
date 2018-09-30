package shishkin.cleanarchitecture.mvi.sl.event;

import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.sl.ui.MaterialDialogExt;

/**
 * Событие - выполнить команду "показать диалог"
 */
@SuppressWarnings("unused")
public class ShowDialogEvent extends AbsEvent {

    private String mTitle;
    private String mMessage;
    private int mButtonPositive = R.string.ok_upper;
    private int mButtonNegative = MaterialDialogExt.NO_BUTTON;
    private boolean mCancelable = false;
    private String mListener;

    public ShowDialogEvent(final int id, final String listener) {
        super(id);
        mListener = listener;
    }

    public ShowDialogEvent(final int id, final String listener, final String title, final String message) {
        this(id, listener);
        mTitle = title;
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getButtonPositive() {
        return mButtonPositive;
    }

    public int getButtonNegative() {
        return mButtonNegative;
    }

    public boolean isCancelable() {
        return mCancelable;
    }

    public String getListener() {
        return mListener;
    }

    public ShowDialogEvent setPositiveButton(int button) {
        mButtonPositive = button;
        return this;
    }

    public ShowDialogEvent setNegativeButton(int button) {
        mButtonNegative = button;
        return this;
    }

    public ShowDialogEvent setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    public ShowDialogEvent setMessage(String message) {
        mMessage = message;
        return this;
    }

    public ShowDialogEvent setTitle(String title) {
        mTitle = title;
        return this;
    }
}
