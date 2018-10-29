package shishkin.cleanarchitecture.mvi.app.screen.fragment.setting;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
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
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.setting.Setting;
import shishkin.cleanarchitecture.mvi.common.LinearLayoutBehavior;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.observe.EditTextObservable;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

@SuppressWarnings("unused")
public class SettingFragment extends AbsContentFragment<SettingModel> implements SettingView {

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    public static final String NAME = SettingFragment.class.getName();
    private LinearLayout mLinearLayout;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLinearLayout = findView(R.id.list);

        mBottomSheetBehavior = LinearLayoutBehavior.from(findView(R.id.bottomSheetContainer));

        if (!ApplicationUtils.checkPermission(ApplicationSpecialistImpl.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            findView(R.id.setting_backup).setEnabled(false);
            findView(R.id.setting_restore).setEnabled(false);
            findView(R.id.db_backup).setEnabled(false);
            findView(R.id.db_restore).setEnabled(false);
        }

        findView(R.id.setting_backup).setOnClickListener(getModel().getPresenter());
        findView(R.id.setting_restore).setOnClickListener(getModel().getPresenter());
        findView(R.id.db_backup).setOnClickListener(getModel().getPresenter());
        findView(R.id.db_restore).setOnClickListener(getModel().getPresenter());
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

    @Override
    public boolean onBackPressed() {
        SLUtil.getViewUnion().switchToTopFragment();
        return true;
    }

    @Override
    public void collapseBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}

