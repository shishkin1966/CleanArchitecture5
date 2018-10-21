package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.observe.AccountObserver;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu.SideMenuFragment;
import shishkin.cleanarchitecture.mvi.app.setting.Setting;
import shishkin.cleanarchitecture.mvi.app.setting.SettingFactory;
import shishkin.cleanarchitecture.mvi.app.setting.SettingOrientation;
import shishkin.cleanarchitecture.mvi.app.specialist.job.JobSpecialistService;
import shishkin.cleanarchitecture.mvi.common.slidingmenu.SlidingMenu;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.BackStack;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsFragment;
import shishkin.cleanarchitecture.mvi.sl.ui.IActivity;

public class MainActivity extends AbsContentActivity<MainModel> implements MainView {

    public static final String NAME = MainActivity.class.getName();

    private Snackbar snackbar;
    private Intent intent;
    private SlidingMenu menu;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Setting setting = SettingFactory.getSetting(SettingOrientation.NAME);
        if (setting.getCurrentValue().equalsIgnoreCase(getString(R.string.orientation_portrait))) {
            lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (setting.getCurrentValue().equalsIgnoreCase(getString(R.string.orientation_landscape))) {
            lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            ((IActivity)SLUtil.getActivity()).lockOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        setStatusBarColor(ViewUtils.getColor(this, R.color.blue_dark));

        prepareSlidingMenu();
        setSideMenuFragment(SideMenuFragment.newInstance());

        final Job.Builder builder = SLUtil.getJobSpecialist().getJobBuilder();
        final Job job = builder
                .setService(JobSpecialistService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(false)
                .setTag(JobSpecialistService.NAME)
                .setTrigger(Trigger.executionWindow(5, 8))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .build();
        SLUtil.getJobSpecialist().schedule(job);

        onNewIntent(getIntent());
    }

    private void prepareSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_container);
    }


    @Override
    public MainModel createModel() {
        return new MainModel(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.intent = intent;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (intent != null) {
            final String action = intent.getAction();
            if ("android.intent.action.MAIN".equalsIgnoreCase(action)) {
                getModel().getRouter().showMainFragment();
            } else if (AccountObserver.ACTION_CLICK.equalsIgnoreCase(action)) {
                getModel().getRouter().showMainFragment();
            }
            intent = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getContentResId() {
        return R.id.content;
    }

    @Override
    public void onConnect() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public void onDisconnect() {
        snackbar = ApplicationUtils.showSnackbar(getRootView(), getString(R.string.network_disconnected), Snackbar.LENGTH_INDEFINITE, ApplicationUtils.MESSAGE_TYPE_WARNING);
    }

    private void setSideMenuFragment(AbsFragment fragment) {
        BackStack.showFragment(this, R.id.menu, fragment, false, false, false, true);
    }

    @Override
    public void hideSideMenu() {
        if (menu.isMenuShowing()) {
            menu.showContent();
        }
    }

    @Override
    public void onUserInteraction() {
        SLUtil.getIdleSpecialist().onUserInteraction();
    }

}
