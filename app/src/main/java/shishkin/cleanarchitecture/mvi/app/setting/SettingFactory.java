package shishkin.cleanarchitecture.mvi.app.setting;

import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Created by Shishkin on 11.02.2018.
 */

public class SettingFactory {

    public static List<Setting> getApplicationSettings() {
        final List<Setting> list = new ArrayList<>();

        final Setting setting = new Setting(Setting.TYPE_TEXT)
                .setTitle(SLUtil.getContext().getString(R.string.setting));
        list.add(setting);

        list.add(getApplicationSetting(SettingPlayMusicEnabled.NAME));
        list.add(getApplicationSetting(SettingOrientation.NAME));

        return list;
    }

    public static void setApplicationSetting(Setting setting) {
        if (setting == null) return;

        SLUtil.getPreferencesSpecialist().putString(setting.getName(), setting.toJson());
    }

    public static Setting getApplicationSetting(final String name) {
        final String s = SLUtil.getPreferencesSpecialist().getString(name, null);
        if (!StringUtils.isNullOrEmpty(s)) {
            return Setting.fromJson(s, Setting.class);
        } else {
            switch (name) {
                case SettingPlayMusicEnabled.NAME:
                    final SettingPlayMusicEnabled applicationSettingPlayMusicEnabled = new SettingPlayMusicEnabled();
                    setApplicationSetting(applicationSettingPlayMusicEnabled);
                    return applicationSettingPlayMusicEnabled;
                case SettingOrientation.NAME:
                    final SettingOrientation applicationSettingOrientation = new SettingOrientation();
                    setApplicationSetting(applicationSettingOrientation);
                    return applicationSettingOrientation;
            }
        }
        return null;
    }

}
