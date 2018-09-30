package shishkin.cleanarchitecture.mvi.sl.ui;

import android.content.Context;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.event.DialogResultEvent;
import shishkin.cleanarchitecture.mvi.sl.mail.DialogResultMail;

public class MaterialDialogExt {

    public static final int NO_BUTTON = -1;
    private static final String ID = "id";
    public static final String BUTTON = "button";
    public static final String POSITIVE = "positive";
    public static final String NEGATIVE = "negative";
    private final static String NEUTRAL = "neutral";

    private int mId;
    private MaterialDialog mMaterialDialog;
    private String mListener;

    public MaterialDialogExt(final Context context, final String listener, final int id,
                             final String title, final String message, final int positiveButton,
                             boolean setCancelable) {
        this(context, listener, id, title, message, positiveButton, NO_BUTTON, NO_BUTTON, setCancelable);
    }

    public MaterialDialogExt(final Context context, final String listener, final int id,
                             final String title, final String message, final int positiveButton,
                             final int negativeButton, boolean setCancelable) {
        this(context, listener, id, title, message, positiveButton, negativeButton, NO_BUTTON, setCancelable);
    }

    public MaterialDialogExt(final Context context, final String listener, final int id,
                             final String title, final String message, final int positiveButton,
                             final int negativeButton, final int neutralButton, boolean setCancelable) {

        mId = id;
        mListener = listener;

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        if (!StringUtils.isNullOrEmpty(title)) {
            builder.title(title);
        }
        if (!StringUtils.isNullOrEmpty(message)) {
            builder.content(message);
        }
        if (positiveButton != NO_BUTTON) {
            builder.positiveText(positiveButton);
            builder.onPositive((dialog, which) -> {
                if (mId > -1) {
                    final Bundle bundle = new Bundle();
                    bundle.putInt(ID, mId);
                    bundle.putString(BUTTON, POSITIVE);
                    if (!StringUtils.isNullOrEmpty(mListener)) {
                        SLUtil.addMail(new DialogResultMail(mListener, new DialogResultEvent(bundle, mId)));
                    }
                }
            });
        }
        if (negativeButton != NO_BUTTON) {
            builder.negativeText(negativeButton);
            builder.onNegative((dialog, which) -> {
                if (mId > -1) {
                    final Bundle bundle = new Bundle();
                    bundle.putInt(ID, mId);
                    bundle.putString(BUTTON, NEGATIVE);
                    if (!StringUtils.isNullOrEmpty(mListener)) {
                        SLUtil.addMail(new DialogResultMail(mListener, new DialogResultEvent(bundle, mId)));
                    }
                }
            });
        }
        if (neutralButton != NO_BUTTON) {
            builder.neutralText(neutralButton);
            builder.onNeutral((dialog, which) -> {
                if (mId > -1) {
                    final Bundle bundle = new Bundle();
                    bundle.putInt(ID, mId);
                    bundle.putString(BUTTON, NEUTRAL);
                    if (!StringUtils.isNullOrEmpty(mListener)) {
                        SLUtil.addMail(new DialogResultMail(mListener, new DialogResultEvent(bundle, mId)));
                    }
                }
            });
        }
        builder.cancelable(setCancelable);

        mMaterialDialog = builder.build();
    }

    public MaterialDialogExt(final Context context, final String listener, final int id,
                             final String title, final String message, final List<String> items, final Integer[] selected, final boolean multiselect, final int positiveButton,
                             final int negativeButton, boolean setCancelable) {

        mId = id;
        mListener = listener;

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        if (!StringUtils.isNullOrEmpty(title)) {
            builder.title(title);
        }
        if (!StringUtils.isNullOrEmpty(message)) {
            builder.content(message);
        }
        builder.items(items);
        if (multiselect) {
            builder.itemsCallbackMultiChoice(null, (dialog, which, text) -> true);
        } else {
            builder.alwaysCallSingleChoiceCallback();
            builder.itemsCallbackSingleChoice(-1, (dialog, view, which, text) -> {
                if (mId > -1) {
                    final Bundle bundle = new Bundle();
                    bundle.putInt(ID, mId);
                    bundle.putString(BUTTON, POSITIVE);
                    final ArrayList<String> list = new ArrayList();
                    list.add(text.toString());
                    bundle.putStringArrayList("list", list);
                    if (!StringUtils.isNullOrEmpty(mListener)) {
                        SLUtil.addMail(new DialogResultMail(mListener, new DialogResultEvent(bundle, mId)));
                    }
                }
                dialog.dismiss();
                return true;
            });
        }
        if (multiselect) {
            if (positiveButton != NO_BUTTON) {
                builder.positiveText(positiveButton);
            }
        }
        if (negativeButton != NO_BUTTON) {
            builder.negativeText(negativeButton);
        }
        if (multiselect) {
            builder.onPositive((dialog, which) -> {
                if (mId > -1) {
                    final Bundle bundle = new Bundle();
                    bundle.putInt("id", mId);
                    bundle.putString(BUTTON, POSITIVE);
                    final ArrayList<String> list = new ArrayList();
                    final ArrayList<CharSequence> itemsCharSequence = dialog.getItems();
                    final Integer[] selected1 = dialog.getSelectedIndices();
                    for (Integer i : selected1) {
                        list.add(itemsCharSequence.get(i).toString());
                    }
                    bundle.putStringArrayList("list", list);
                    if (!StringUtils.isNullOrEmpty(mListener)) {
                        SLUtil.addMail(new DialogResultMail(mListener, new DialogResultEvent(bundle, mId)));
                    }
                }
            });
        }
        builder.onNegative((dialog, which) -> {
            if (mId > -1) {
                final Bundle bundle = new Bundle();
                bundle.putInt(ID, mId);
                bundle.putString(BUTTON, NEGATIVE);
                if (!StringUtils.isNullOrEmpty(mListener)) {
                    SLUtil.addMail(new DialogResultMail(mListener, new DialogResultEvent(bundle, mId)));
                }
            }
        });
        builder.cancelable(setCancelable);

        mMaterialDialog = builder.build();

        if (selected != null) {
            mMaterialDialog.setSelectedIndices(selected);
        }
    }


    public MaterialDialogExt(final Context context, final String listener, final int id,
                             final String title, final String message, final String edittext, final String hint, final int input_type, final int positiveButton,
                             final int negativeButton, boolean setCancelable) {

        mId = id;
        mListener = listener;

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        if (!StringUtils.isNullOrEmpty(title)) {
            builder.title(title);
        }
        if (!StringUtils.isNullOrEmpty(message)) {
            builder.content(message);
        }
        builder.positiveText(positiveButton);
        if (negativeButton != NO_BUTTON) {
            builder.negativeText(negativeButton);
        }
        builder.inputType(input_type);
        builder.input(hint, edittext, (dialog, input) -> {
        });
        builder.onPositive((dialog, which) -> {
            if (mId > -1) {
                final Bundle bundle = new Bundle();
                bundle.putInt(ID, mId);
                bundle.putString(BUTTON, POSITIVE);
                bundle.putString("object", dialog.getInputEditText().getText().toString());
                if (!StringUtils.isNullOrEmpty(mListener)) {
                    SLUtil.addMail(new DialogResultMail(mListener, new DialogResultEvent(bundle, mId)));
                }
            }
        });
        builder.onNegative((dialog, which) -> {
            if (mId > -1) {
                final Bundle bundle = new Bundle();
                bundle.putInt(ID, mId);
                bundle.putString(BUTTON, NEGATIVE);
                if (!StringUtils.isNullOrEmpty(mListener)) {
                    SLUtil.addMail(new DialogResultMail(mListener, new DialogResultEvent(bundle, mId)));
                }
            }
        });
        builder.cancelable(setCancelable);

        mMaterialDialog = builder.build();
    }

    public void show() {
        if (mMaterialDialog != null) {
            float size = ViewUtils.px2sp(mMaterialDialog.getContext(), mMaterialDialog.getContext().getResources().getDimension(R.dimen.text_size_large));
            mMaterialDialog.getActionButton(DialogAction.POSITIVE).setTextSize(size);
            mMaterialDialog.getActionButton(DialogAction.POSITIVE).setTextColor(ViewUtils.getColor(mMaterialDialog.getContext(), R.color.blue));
            mMaterialDialog.getActionButton(DialogAction.NEGATIVE).setTextSize(size);
            mMaterialDialog.getActionButton(DialogAction.NEGATIVE).setTextColor(ViewUtils.getColor(mMaterialDialog.getContext(), R.color.blue));
            mMaterialDialog.getActionButton(DialogAction.NEUTRAL).setTextSize(size);
            mMaterialDialog.getActionButton(DialogAction.NEUTRAL).setTextColor(ViewUtils.getColor(mMaterialDialog.getContext(), R.color.blue));
            mMaterialDialog.getContentView().setTextSize(size);
            size = ViewUtils.px2sp(mMaterialDialog.getContext(), mMaterialDialog.getContext().getResources().getDimension(R.dimen.text_size_xlarge));
            mMaterialDialog.getTitleView().setTextSize(size);
            mMaterialDialog.show();
        }
    }

}
