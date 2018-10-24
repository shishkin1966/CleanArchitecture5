package shishkin.cleanarchitecture.mvi.app.setting;

import android.Manifest;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.BuildConfig;
import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.CloseUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;

/**
 * Created by Shishkin on 11.02.2018.
 */

public class SettingFactory {

    public static List<Setting> getSettings() {
        final List<Setting> list = new ArrayList<>();

        final Setting setting = new Setting(Setting.TYPE_TEXT)
                .setTitle(SLUtil.getContext().getString(R.string.setting)+ " (версия программы "+ BuildConfig.VERSION_NAME+")");
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

    public static void backup() {
        if (!ApplicationUtils.checkPermission(ApplicationSpecialistImpl.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return;
        }

        final List<Setting> list = getSettings();
        final Type listType = new TypeToken<List<Setting>>() {
        }.getType();
        final Gson gson = new Gson();
        final String json = gson.toJson(list, listType);

        final File file = new File(ApplicationSpecialistImpl.getInstance().getExternalDataPath(), "settings.json");
        if (file.exists()) {
            file.delete();
        }

        if (writeStringAsFile(ApplicationSpecialistImpl.getInstance().getExternalDataPath(), "settings.json", json)) {
            ApplicationUtils.showToast(SLUtil.getContext().getString(R.string.setting_backup), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_INFO);
        }
    }

    public static void restore() {
        if (!ApplicationUtils.checkPermission(ApplicationSpecialistImpl.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return;
        }

        final File file = new File(ApplicationSpecialistImpl.getInstance().getExternalDataPath(), "settings.json");
        if (!file.exists()) {
            SLUtil.getActivityUnion().showFlashbar(new ShowMessageEvent(SLUtil.getContext().getString(R.string.warning), SLUtil.getContext().getString(R.string.setting_error), ApplicationUtils.MESSAGE_TYPE_ERROR));
            return;
        }

        final String json = readFileAsString(ApplicationSpecialistImpl.getInstance().getExternalDataPath(), "settings.json");
        if (!StringUtils.isNullOrEmpty(json)) {
            final Type listType = new TypeToken<List<Setting>>() {
            }.getType();
            final Gson gson = new Gson();
            final List<Setting> list = gson.fromJson(json, listType);
            for (Setting setting : list) {
                setSetting(setting);
            }
            ApplicationUtils.showToast(SLUtil.getContext().getString(R.string.setting_restore), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_INFO);
        }
    }

    private static boolean writeStringAsFile(String path, String file, String content) {
        FileWriter out = null;
        try {
            out = new FileWriter(new File(path, file));
            out.write(content);
        } catch (IOException e) {
            SLUtil.onError(SettingFactory.class.getName(), e);
            return false;
        } finally {
            CloseUtils.close(out);
        }
        return true;
    }

    private static String readFileAsString(String path, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(path, fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);
        } catch (FileNotFoundException e) {
            SLUtil.onError(SettingFactory.class.getName(), e);
        } catch (IOException e) {
            SLUtil.onError(SettingFactory.class.getName(), e);
        } finally {
            CloseUtils.close(in);
        }
        return stringBuilder.toString();
    }


}
