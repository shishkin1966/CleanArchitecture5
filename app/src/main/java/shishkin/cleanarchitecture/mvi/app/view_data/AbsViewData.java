package shishkin.cleanarchitecture.mvi.app.view_data;

import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.request.Request;

public abstract class AbsViewData<T extends Parcelable> {
    private int mSort = 0;
    private int mFilter = 0;
    private List<T> mData;

    public int getSort() {
        return mSort;
    }

    public void setSort(int sort) {
        this.mSort = sort;
    }

    public int getFilter() {
        return mFilter;
    }

    public void setFilter(int filter) {
        this.mFilter = filter;
    }

    public abstract String getName();

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public String getPattern() {
        return SLUtil.getPreferencesSpecialist().getString(getName(), null);
    }

    public void setPattern(String pattern) {
        SLUtil.getPreferencesSpecialist().putString(getName(), pattern);
    }
}
