package shishkin.cleanarchitecture.mvi.app.screen.fragment.setting;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.setting.ApplicationSetting;

public interface SettingView {
    void refreshViews(List<ApplicationSetting> settings);
}
