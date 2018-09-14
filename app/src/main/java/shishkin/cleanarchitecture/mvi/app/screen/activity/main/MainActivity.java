package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import android.content.pm.ActivityInfo;
import android.os.Bundle;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentActivity;

public class MainActivity extends AbsContentActivity<MainModel> {

    public static final String NAME = MainActivity.class.getName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setModel(new MainModel(this));

        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getModel().getRouter().showMainFragment();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getContentResId() {
        return R.id.content;
    }
}
