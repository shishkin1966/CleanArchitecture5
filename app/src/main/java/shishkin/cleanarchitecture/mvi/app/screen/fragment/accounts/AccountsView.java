package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface AccountsView extends IFragment {
    void refreshAccounts(List<Account> list);

    void refreshBalance(List<MviDao.Balance> list);

    void collapseBottomSheet();
}
