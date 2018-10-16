package shishkin.cleanarchitecture.mvi.app.screen.fragment.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


import shishkin.cleanarchitecture.mvi.app.ApplicationConstant;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.setting.Setting;
import shishkin.cleanarchitecture.mvi.app.setting.SettingFactory;
import shishkin.cleanarchitecture.mvi.app.setting.SettingOrientation;
import shishkin.cleanarchitecture.mvi.sl.event.DialogResultEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowListDialogEvent;
import shishkin.cleanarchitecture.mvi.sl.observe.EditTextObservable;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.ui.DialogResultListener;
import shishkin.cleanarchitecture.mvi.sl.ui.MaterialDialogExt;

public class SettingPresenter extends AbsPresenter<SettingModel> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, DialogResultListener, Observer {
    public static final String NAME = SettingPresenter.class.getName();

    public SettingPresenter(SettingModel model) {
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
        final Setting setting = (Setting) v.getTag();
        if (setting != null) {
            SLUtil.getActivityUnion().showListDialog(new ShowListDialogEvent(setting.getId(), getName(), setting.getTitle(), null, setting.getValues(), MaterialDialogExt.NO_BUTTON, MaterialDialogExt.NO_BUTTON, true));
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
