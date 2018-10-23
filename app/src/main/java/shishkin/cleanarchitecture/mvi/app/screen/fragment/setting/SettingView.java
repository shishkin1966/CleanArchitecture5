package shishkin.cleanarchitecture.mvi.app.screen.fragment.setting;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.setting.Setting;

public interface SettingView {
    void refreshViews(List<Setting> settings);

    void collapseBottomSheet();


}
