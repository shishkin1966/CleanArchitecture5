package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsFragment;

public class SideMenuFragment extends AbsFragment<SideMenuModel> {

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sidemenu, container, false);
    }

    @Override
    public SideMenuModel createModel() {
        return new SideMenuModel(this);
    }
}
