package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface SideMenuView extends IFragment {
    void accountsChanged(List<MviDao.Balance> list);
}
