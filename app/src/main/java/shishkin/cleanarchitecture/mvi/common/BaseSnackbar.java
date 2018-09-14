package shishkin.cleanarchitecture.mvi.common;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;

public class BaseSnackbar {

    @CheckResult
    @NonNull
    public static Snackbar make(@NonNull final View view, @StringRes final int titleRes, final int duration, final int
            type) {
        final Context context = view.getContext();
        return make(view, context.getText(titleRes), duration, type);
    }

    @CheckResult
    public static Snackbar make(@NonNull final View view, @NonNull final CharSequence title, final int duration,
                                final int type) {
        final Snackbar snackbar = Snackbar.make(view, title, duration);
        final View snackbarView = snackbar.getView();
        final TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ViewUtils.getColor(view.getContext(), R.color.white));
        if (ViewUtils.diagonalInch(view.getContext()) < 5) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getDimensionPx(view.getContext(), R.dimen.text_size));
        }
        if (ApplicationUtils.hasJellyBeanMR1()) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        int backgroundColor;
        switch (type) {
            case ApplicationUtils.MESSAGE_TYPE_ERROR:
                snackbar.setActionTextColor(ViewUtils.getColor(view.getContext(), R.color.gray));
                backgroundColor = R.color.red;
                break;

            case ApplicationUtils.MESSAGE_TYPE_WARNING:
                snackbar.setActionTextColor(ViewUtils.getColor(view.getContext(), R.color.gray));
                backgroundColor = R.color.orange;
                break;

            default:
                snackbar.setActionTextColor(ViewUtils.getColor(view.getContext(), R.color.green));
                backgroundColor = R.color.blue;
        }
        snackbarView.setBackgroundColor(ViewUtils.getColor(view.getContext(), backgroundColor));
        textView.setSingleLine(false);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        return snackbar;
    }

    private BaseSnackbar() {
    }

}
