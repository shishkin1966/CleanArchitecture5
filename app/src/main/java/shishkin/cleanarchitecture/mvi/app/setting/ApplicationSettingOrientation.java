package shishkin.cleanarchitecture.mvi.app.setting;

import java.util.ArrayList;
import java.util.Arrays;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationConstant;
import shishkin.cleanarchitecture.mvi.app.SLUtil;

/**
 * Created by Shishkin on 11.02.2018.
 */

public class ApplicationSettingOrientation extends ApplicationSetting {

    public static final String NAME = "ApplicationSettingOrientation";

    private String[] values = SLUtil.getContext().getResources().getStringArray(R.array.orientation_key);
    private String currentValue = "Вертикальная";

    public ApplicationSettingOrientation() {
        super(ApplicationSetting.TYPE_LIST);

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(values));

        setName(NAME);
        setTitle(SLUtil.getContext().getString(R.string.settings_orientattion));
        setId(ApplicationConstant.APPLICATION_SETTING_ORIENTATION);
        setValues(arrayList);
        setCurrentValue(currentValue);
    }
}
