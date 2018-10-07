package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.viewdata.AccountsViewData;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface AccountsView extends IFragment {
    void refreshBalance(List<MviDao.Balance> list);

    void collapseBottomSheet();

    void refreshViews(AccountsViewData setting);

    void hideMessage();
}
