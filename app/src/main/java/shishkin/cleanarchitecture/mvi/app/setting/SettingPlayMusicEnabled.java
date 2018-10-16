package shishkin.cleanarchitecture.mvi.app.setting;

import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationConstant;
import shishkin.cleanarchitecture.mvi.app.SLUtil;

/**
 * Created by Shishkin on 11.02.2018.
 */

public class SettingPlayMusicEnabled extends Setting {

    public static final String NAME = "SettingPlayMusicEnabled";

    public SettingPlayMusicEnabled() {
        super(Setting.TYPE_SWITCH);

        setName(NAME);
        setTitle(SLUtil.getContext().getString(R.string.settings_show_screenshot));
        setCurrentValue("true");
        setId(ApplicationConstant.SETTING_PLAY_MUSIC_ENABLED);
    }
}
