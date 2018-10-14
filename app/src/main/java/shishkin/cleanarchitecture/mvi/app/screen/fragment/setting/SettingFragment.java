package shishkin.cleanarchitecture.mvi.app.screen.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.setting.Setting;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.observe.EditTextObservable;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

@SuppressWarnings("unused")
public class SettingFragment extends AbsContentFragment<SettingModel> implements SettingView {

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    public static final String NAME = SettingFragment.class.getName();
    private LinearLayout mLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLinearLayout = findView(R.id.list);
    }

    @Override
    public void refreshViews(List<Setting> settings) {
        mLinearLayout.removeAllViews();

        for (Setting setting : settings) {
            final View view = getView(mLinearLayout, setting);
            if (view != null) {
                mLinearLayout.addView(view);
            }
        }
    }

    private View getView(final ViewGroup parent, final Setting setting) {
        View view = null;
        TextView titleView;
        String currentValue;

        switch (setting.getType()) {
            case Setting.TYPE_TEXT:
                view = getLayoutInflater().inflate(R.layout.setting_item_text, parent, false);
                titleView = ViewUtils.findView(view, R.id.item_title);
                titleView.setText(setting.getTitle());
                break;

            case Setting.TYPE_SWITCH:
                view = getLayoutInflater().inflate(R.layout.setting_item_switch, parent, false);
                titleView = ViewUtils.findView(view, R.id.item_title);
                titleView.setText(setting.getTitle());

                final SwitchCompat valueView = ViewUtils.findView(view, R.id.item_switch);
                currentValue = setting.getCurrentValue();
                valueView.setChecked(Boolean.valueOf(currentValue));
                valueView.setTag(setting);
                valueView.setOnCheckedChangeListener(getModel().getPresenter());
                break;

            case Setting.TYPE_LIST:
                view = getLayoutInflater().inflate(R.layout.setting_item_list, parent, false);
                ViewUtils.findView(view, R.id.ll).setTag(setting);
                ViewUtils.findView(view, R.id.ll).setOnClickListener(getModel().getPresenter());
                titleView = ViewUtils.findView(view, R.id.item_title);
                titleView.setText(setting.getTitle());
                titleView = ViewUtils.findView(view, R.id.item_value);
                titleView.setText(setting.getCurrentValue());
                break;

            case Setting.TYPE_EDIT:
                view = getLayoutInflater().inflate(R.layout.setting_item_edit, parent, false);
                titleView = ViewUtils.findView(view, R.id.item_title);
                titleView.setText(setting.getTitle());
                final EditText editView = ViewUtils.findView(view, R.id.item_edit);
                editView.setText(setting.getCurrentValue());
                editView.setSelection(editView.getText().toString().length());
                if (setting.getInputType() == 0) {
                    setting.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                editView.setInputType(setting.getInputType());
                editView.setTag(setting);
                new EditTextObservable(getModel().getPresenter(), editView);
                break;
            default:
                break;
        }
        return view;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public SettingModel createModel() {
        return new SettingModel(this);
    }
}

