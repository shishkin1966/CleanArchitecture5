package shishkin.cleanarchitecture.mvi.app.setting;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Created by Shishkin on 11.02.2018.
 */

public class ApplicationSettingFactory {

    public static List<ApplicationSetting> getApplicationSettings() {
        final List<ApplicationSetting> list = new ArrayList<>();


        final Context context = SLUtil.getContext();
        if (context == null) return list;

        list.add(getApplicationSetting(ApplicationSettingPlayMusicEnabled.NAME));

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
                    return new ApplicationSettingPlayMusicEnabled();
            }
        }
        return null;
    }

}
