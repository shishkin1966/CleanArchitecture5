package shishkin.cleanarchitecture.mvi.app.setting;

import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationConstant;
import shishkin.cleanarchitecture.mvi.app.SLUtil;

/**
 * Created by Shishkin on 11.02.2018.
 */

public class ApplicationSettingPlayMusicEnabled extends ApplicationSetting {

    public static final String NAME = "ApplicationSettingPlayMusicEnabled";

    public ApplicationSettingPlayMusicEnabled() {
        super(ApplicationSetting.TYPE_SWITCH);

        setName(NAME);
        setTitle(SLUtil.getContext().getString(R.string.settings_show_screenshot));
        setCurrentValue("true");
        setId(ApplicationConstant.APPLICATION_SETTING_PLAY_MUSIC_ENABLED);
    }
}
