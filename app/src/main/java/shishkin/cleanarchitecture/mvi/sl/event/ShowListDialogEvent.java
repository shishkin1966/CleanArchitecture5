package shishkin.cleanarchitecture.mvi.sl.event;

import java.util.ArrayList;

/**
 * Событие - выполнить команду "показать List диалог"
 */
public class ShowListDialogEvent extends ShowDialogEvent {

    private ArrayList<String> mList;
    private Integer[] mSelected;
    private boolean mMultiselect = false;

    public ShowListDialogEvent(final int id, final String listener) {
        super(id, listener);
    }

    public ShowListDialogEvent(final int id, final String listener, final String title, final String message, final ArrayList<String> list, final Integer[] selected, final boolean multiselect, final int button_positive, final int button_negative, final boolean setCancelable) {
        super(id, listener, title, message);

        mList = list;
        mSelected = selected;
        mMultiselect = multiselect;

        setPositiveButton(button_positive);
        setNegativeButton(button_negative);
        setCancelable(setCancelable);
    }

    public ShowListDialogEvent(final int id, final String listener, final String title, final String message, final ArrayList<String> list, final int button_positive, final int button_negative, final boolean setCancelable) {
        super(id, listener, title, message);

        mList = list;
        mMultiselect = false;

        setPositiveButton(button_positive);
        setNegativeButton(button_negative);
        setCancelable(setCancelable);
    }

    public ArrayList<String> getList() {
        return mList;
    }

    public Integer[] getSelected() {
        return mSelected;
    }

    public boolean isMultiselect() {
        return mMultiselect;
    }

    public ShowListDialogEvent setList(ArrayList<String> list) {
        mList = list;
        return this;
    }

    public ShowListDialogEvent setSelected(Integer[] selected) {
        mSelected = selected;
        return this;
    }

    public ShowListDialogEvent setMultiselect(boolean multiselect) {
        mMultiselect = multiselect;
        return this;
    }
}
