package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.request.GetBalanceRequest;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class SideMenuInteractor implements ModelInteractor {

    public void getBalance(ResponseListener listener) {
        SLUtil.getDbProvider().request(new GetBalanceRequest(listener));
    }
}
