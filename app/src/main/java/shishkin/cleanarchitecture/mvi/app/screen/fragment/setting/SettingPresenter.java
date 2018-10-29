package shishkin.cleanarchitecture.mvi.app.screen.fragment.setting;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationConstant;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.request.DbBackupRequest;
import shishkin.cleanarchitecture.mvi.app.request.DbRestoreRequest;
import shishkin.cleanarchitecture.mvi.app.setting.Setting;
import shishkin.cleanarchitecture.mvi.app.setting.SettingFactory;
import shishkin.cleanarchitecture.mvi.app.setting.SettingOrientation;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.event.DialogResultEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowListDialogEvent;
import shishkin.cleanarchitecture.mvi.sl.observe.EditTextObservable;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.ui.DialogResultListener;
import shishkin.cleanarchitecture.mvi.sl.ui.IActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.MaterialDialogExt;

public class SettingPresenter extends AbsPresenter<SettingModel> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, DialogResultListener, Observer {
    public static final String NAME = SettingPresenter.class.getName();

    SettingPresenter(SettingModel model) {
        super(model);
    }

    @Override
    public boolean isRegister() {
        return true;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onStart() {
        getModel().getView().refreshViews(SettingFactory.getSettings());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        final Setting setting = (Setting) buttonView.getTag();
        if (setting != null) {
            setting.setCurrentValue(String.valueOf(isChecked));

            SettingFactory.setSetting(setting);
        }
    }

    @Override
    public void onClick(View v) {
        getModel().getView().collapseBottomSheet();
        switch (v.getId()) {
            case R.id.setting_backup:
                SettingFactory.backup();
                break;

            case R.id.setting_restore:
                SettingFactory.restore();
                getModel().getView().refreshViews(SettingFactory.getSettings());
                break;

            case R.id.db_backup:
                SLUtil.getRequestSpecialist().request(this, new DbBackupRequest());
                break;

            case R.id.db_restore:
                SLUtil.getRequestSpecialist().request(this, new DbRestoreRequest());
                break;

            default:
                final Setting setting = (Setting) v.getTag();
                if (setting != null) {
                    SLUtil.getViewUnion().showListDialog(new ShowListDialogEvent(setting.getId(), getName(), setting.getTitle(), null, setting.getValues(), MaterialDialogExt.NO_BUTTON, MaterialDialogExt.NO_BUTTON, true));
                }
                break;
        }
    }

    @Override
    public void onDialogResult(DialogResultEvent event) {
        if (event.getId() == ApplicationConstant.SETTING_ORIENTATION) {
            final Bundle bundle = event.getResult();
            if (MaterialDialogExt.POSITIVE.equals(bundle.getString(MaterialDialogExt.BUTTON))) {
                final ArrayList<String> list = bundle.getStringArrayList("list");
                if (list != null && list.size() == 1) {
                    final String currentValue = list.get(0);
                    final Setting setting = SettingFactory.getSetting(SettingOrientation.NAME);
                    setting.setCurrentValue(currentValue);
                    SettingFactory.setSetting(setting);
                    getModel().getView().refreshViews(SettingFactory.getSettings());
                    final IActivity activity = SLUtil.getActivity();
                    if (activity != null) {
                        if (setting.getCurrentValue().equalsIgnoreCase(SLUtil.getContext().getString(R.string.orientation_portrait))) {
                            activity.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        } else if (setting.getCurrentValue().equalsIgnoreCase(SLUtil.getContext().getString(R.string.orientation_landscape))) {
                            activity.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        } else {
                            activity.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        }
                        if (!ViewUtils.isPhone(SLUtil.getContext()) && ViewUtils.is10inchTablet(SLUtil.getContext())) {
                            ((AppCompatActivity) activity).recreate();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        final String value = (String) arg;
        EditTextObservable observable = (EditTextObservable) o;
        final Setting setting = (Setting) observable.getView().getTag();
        if (setting != null) {
            setting.setCurrentValue(value);
            SettingFactory.setSetting(setting);
        }

    }
}
