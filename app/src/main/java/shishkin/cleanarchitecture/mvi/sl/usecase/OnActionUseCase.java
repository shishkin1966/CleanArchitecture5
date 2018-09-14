package shishkin.cleanarchitecture.mvi.sl.usecase;

import android.content.Context;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.event.OnActionEvent;

/**
 * Команда - выполнить Action
 */
public class OnActionUseCase extends AbsUseCase {

    public static final String NAME = OnActionUseCase.class.getName();

    public static void onClick(final OnActionEvent event) {
        final String action = event.getAction();
        final Context context = SLUtil.getContext();

        if (context != null) {
            if (action.equals(context.getString(R.string.exit))) {
                ApplicationSpecialistImpl.getInstance().finish();
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}