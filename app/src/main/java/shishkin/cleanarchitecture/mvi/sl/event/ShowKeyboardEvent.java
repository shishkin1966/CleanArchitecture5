package shishkin.cleanarchitecture.mvi.sl.event;

import android.widget.EditText;

/**
 * Событие - выполнить команду "показать клавиатуру"
 */
public class ShowKeyboardEvent extends AbsEvent {

    private EditText mEditText;

    public ShowKeyboardEvent(final EditText view) {
        mEditText = view;
    }

    public EditText getView() {
        return mEditText;
    }
}
