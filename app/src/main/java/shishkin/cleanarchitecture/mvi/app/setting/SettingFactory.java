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

    public static List<Setting> getSettings() {
        final List<Setting> list = new ArrayList<>();

        final Setting setting = new Setting(Setting.TYPE_TEXT)
                .setTitle(SLUtil.getContext().getString(R.string.setting));
        list.add(setting);

        list.add(getSetting(SettingPlayMusicEnabled.NAME));
        list.add(getSetting(SettingOrientation.NAME));

        return list;
    }

    public static void setSetting(Setting setting) {
        if (setting == null) return;

        SLUtil.getPreferencesSpecialist().putString(setting.getName(), setting.toJson());
    }

    public static Setting getSetting(final String name) {
        final String s = SLUtil.getPreferencesSpecialist().getString(name, null);
        if (!StringUtils.isNullOrEmpty(s)) {
            return Setting.fromJson(s, Setting.class);
        } else {
            switch (name) {
                case SettingPlayMusicEnabled.NAME:
                    final SettingPlayMusicEnabled settingPlayMusicEnabled = new SettingPlayMusicEnabled();
                    setSetting(settingPlayMusicEnabled);
                    return settingPlayMusicEnabled;
                case SettingOrientation.NAME:
                    final SettingOrientation settingOrientation = new SettingOrientation();
                    setSetting(settingOrientation);
                    return settingOrientation;
            }
        }
        return null;
    }

}
