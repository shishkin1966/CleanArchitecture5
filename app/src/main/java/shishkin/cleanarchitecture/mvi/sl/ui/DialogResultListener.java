package shishkin.cleanarchitecture.mvi.sl.ui;

import shishkin.cleanarchitecture.mvi.sl.Validated;
import shishkin.cleanarchitecture.mvi.sl.event.DialogResultEvent;

/**
 * Created by Shishkin on 17.11.2017.
 */

public interface DialogResultListener extends Validated {
    void onDialogResult(DialogResultEvent event);
}
