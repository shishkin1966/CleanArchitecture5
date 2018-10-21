package shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.observe.EditTextObservable;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class CalcFragment extends AbsContentFragment<CalcModel> implements CalcView {

    public static CalcFragment newInstance() {
        return new CalcFragment();
    }

    private TextView sumView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText itemView1 = findView(R.id.item_edit_1);
        itemView1.setTag("1");

        EditText itemView2 = findView(R.id.item_edit_2);
        itemView2.setTag("2");

        EditText itemView3 = findView(R.id.item_edit_3);
        itemView3.setTag("3");

        EditText itemView4 = findView(R.id.item_edit_4);
        itemView4.setTag("4");

        EditText itemView5 = findView(R.id.item_edit_5);
        itemView5.setTag("5");

        sumView = findView(R.id.sum);

        new EditTextObservable(getModel().getPresenter(), itemView1, TimeUnit.SECONDS.toMillis(1));
        new EditTextObservable(getModel().getPresenter(), itemView2, TimeUnit.SECONDS.toMillis(1));
        new EditTextObservable(getModel().getPresenter(), itemView3, TimeUnit.SECONDS.toMillis(1));
        new EditTextObservable(getModel().getPresenter(), itemView4, TimeUnit.SECONDS.toMillis(1));
        new EditTextObservable(getModel().getPresenter(), itemView5, TimeUnit.SECONDS.toMillis(1));
    }

    @Override
    public String getName() {
        return CalcFragment.class.getName();
    }

    @Override
    public CalcModel createModel() {
        return new CalcModel(this);
    }

    @Override
    public void refreshViews(CalcViewData viewData) {
        sumView.setText(String.valueOf(viewData.getSum()));
    }

    @Override
    public boolean onBackPressed() {
        SLUtil.getActivityUnion().switchToTopFragment();
        return true;
    }
}
