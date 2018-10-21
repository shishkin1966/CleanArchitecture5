package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;

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

public class MainActivity extends AbsContentActivity<MainModel> implements MainView {

    public static final String NAME = MainActivity.class.getName();

    private Snackbar snackbar;
    private Intent intent;
    private SlidingMenu menu;
    private int idLayout = R.layout.activity_main;
    private int orientation = ViewUtils.getOrientation(SLUtil.getContext());

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        Configuration conf = getResources().getConfiguration();
        final Setting setting = SettingFactory.getSetting(SettingOrientation.NAME);
        if (setting.getCurrentValue().equalsIgnoreCase(getString(R.string.orientation_portrait))) {
            orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (setting.getCurrentValue().equalsIgnoreCase(getString(R.string.orientation_landscape))) {
            orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if (!ViewUtils.isPhone(this)) {
                idLayout = R.layout.activity_main_land;
            }
        } else {
            lockOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        setContentView(idLayout);

        setMenu();

        setStatusBarColor(ViewUtils.getColor(this, R.color.blue_dark));

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
        if (menu == null) {
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
    }

    @Override
    public MainModel createModel() {
        return new MainModel(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.intent = intent;
    }

    private void setMenu() {
        if (!ViewUtils.isPhone(this) && orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        } else {
            prepareSlidingMenu();
        }
        ViewGroup v = findViewById(R.id.menu);
        if (v != null) {
            setSideMenuFragment(R.id.menu, SideMenuFragment.newInstance());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setMenu();

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
        if (menu != null) {
            if (menu.isMenuShowing()) {
                menu.showContent();
            } else {
                super.onBackPressed();
            }
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

    private void setSideMenuFragment(int idRes, AbsFragment fragment) {
        BackStack.showFragment(this, idRes, fragment, false, false, false, true);
    }

    @Override
    public void hideSideMenu() {
        if (menu != null) {
            if (menu.isMenuShowing()) {
                menu.showContent();
            }
        }
    }

    @Override
    public void onUserInteraction() {
        SLUtil.getIdleSpecialist().onUserInteraction();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (menu != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                menu.setBehindOffsetRes(R.dimen.slidingmenu_offset_land);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
            }
        }
    }
}
