package shishkin.cleanarchitecture.mvi.app.setting;

import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Created by Shishkin on 11.02.2018.
 */

public class ApplicationSettingFactory {

    public static List<ApplicationSetting> getApplicationSettings() {
        final List<ApplicationSetting> list = new ArrayList<>();

        final ApplicationSetting setting = new ApplicationSetting(ApplicationSetting.TYPE_TEXT)
                .setTitle(SLUtil.getContext().getString(R.string.setting));
        list.add(setting);

        list.add(getApplicationSetting(ApplicationSettingPlayMusicEnabled.NAME));
        list.add(getApplicationSetting(ApplicationSettingOrientation.NAME));

        return list;
    }

    public static void setApplicationSetting(ApplicationSetting setting) {
        if (setting == null) return;

        SLUtil.getPreferencesSpecialist().putString(setting.getName(), setting.toJson());
    }

    public static ApplicationSetting getApplicationSetting(final String name) {
        final String s = SLUtil.getPreferencesSpecialist().getString(name, null);
        if (!StringUtils.isNullOrEmpty(s)) {
            return ApplicationSetting.fromJson(s, ApplicationSetting.class);
        } else {
            switch (name) {
                case ApplicationSettingPlayMusicEnabled.NAME:
                    final ApplicationSettingPlayMusicEnabled applicationSettingPlayMusicEnabled = new ApplicationSettingPlayMusicEnabled();
                    setApplicationSetting(applicationSettingPlayMusicEnabled);
                    return applicationSettingPlayMusicEnabled;
                case ApplicationSettingOrientation.NAME:
                    final ApplicationSettingOrientation applicationSettingOrientation = new ApplicationSettingOrientation();
                    setApplicationSetting(applicationSettingOrientation);
                    return applicationSettingOrientation;
            }
        }
        return null;
    }

}
