package shishkin.cleanarchitecture.mvi.app.observe;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;

public interface AccountsBalanceListener {
    void showAccountsBalance(List<MviDao.Balance> list);
}
