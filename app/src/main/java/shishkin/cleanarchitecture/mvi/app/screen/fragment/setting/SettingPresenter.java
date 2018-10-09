package shishkin.cleanarchitecture.mvi.app.screen.fragment.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;


import java.util.ArrayList;


import shishkin.cleanarchitecture.mvi.app.ApplicationConstant;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.setting.ApplicationSetting;
import shishkin.cleanarchitecture.mvi.app.setting.ApplicationSettingFactory;
import shishkin.cleanarchitecture.mvi.app.setting.ApplicationSettingOrientation;
import shishkin.cleanarchitecture.mvi.sl.event.DialogResultEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowListDialogEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.ui.DialogResultListener;
import shishkin.cleanarchitecture.mvi.sl.ui.MaterialDialogExt;

public class SettingPresenter extends AbsPresenter<SettingModel> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, DialogResultListener {
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
        getModel().getView().refreshViews(ApplicationSettingFactory.getApplicationSettings());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        final ApplicationSetting setting = (ApplicationSetting) buttonView.getTag();
        if (setting != null) {
            setting.setCurrentValue(String.valueOf(isChecked));

            ApplicationSettingFactory.setApplicationSetting(setting);
        }
    }

    @Override
    public void onClick(View v) {
        final ApplicationSetting setting = (ApplicationSetting) v.getTag();
        if (setting != null) {
            SLUtil.getActivityUnion().showListDialog(new ShowListDialogEvent(setting.getId(), getName(), setting.getTitle(), null, setting.getValues(), MaterialDialogExt.NO_BUTTON, MaterialDialogExt.NO_BUTTON, true));
        }
    }

    @Override
    public void onDialogResult(DialogResultEvent event) {
        if (event.getId() == ApplicationConstant.APPLICATION_SETTING_ORIENTATION) {
            final Bundle bundle = event.getResult();
            if (MaterialDialogExt.POSITIVE.equals(bundle.getString(MaterialDialogExt.BUTTON))) {
                final ArrayList<String> list = bundle.getStringArrayList("list");
                if (list != null && list.size() == 1) {
                    final String currentValue = list.get(0);
                    final ApplicationSetting setting = ApplicationSettingFactory.getApplicationSetting(ApplicationSettingOrientation.NAME);
                    setting.setCurrentValue(currentValue);
                    ApplicationSettingFactory.setApplicationSetting(setting);
                    getModel().getView().refreshViews(ApplicationSettingFactory.getApplicationSettings());
                }
            }
        }
    }
}
