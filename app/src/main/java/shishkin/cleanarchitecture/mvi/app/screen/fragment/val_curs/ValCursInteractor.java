package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ValCursInteractor implements ModelInteractor {

    public void getValCurs(String listener, String date) {
        SLUtil.getRepository().getValCurs(listener, date);
    }
}
