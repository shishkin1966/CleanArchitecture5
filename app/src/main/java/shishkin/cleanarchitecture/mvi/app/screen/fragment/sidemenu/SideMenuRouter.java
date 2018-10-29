package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.model.BaseModelRouter;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation.CalcFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.contact.ContactFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies.DigitalCurrenciesFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.map.MapFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.paging.PagingFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google.PagingGoogleFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.setting.SettingFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs.ValCursFragment;
import shishkin.cleanarchitecture.mvi.sl.BackStack;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

class SideMenuRouter extends BaseModelRouter {

    SideMenuRouter(AbsModel model) {
        super(model);
    }

    void showDigitalCurrencies() {
        if (!BackStack.isCurrentFragment(SLUtil.getActivity(), DigitalCurrenciesFragment.NAME)) {
            showFragment(DigitalCurrenciesFragment.newInstance());
        }
    }

    void showValCurs() {
        if (!BackStack.isCurrentFragment(SLUtil.getActivity(), ValCursFragment.NAME)) {
            showFragment(ValCursFragment.newInstance());
        }
    }

    void showMap() {
        if (!BackStack.isCurrentFragment(SLUtil.getActivity(), MapFragment.NAME)) {
            showFragment(MapFragment.newInstance());
        }
    }

    void showSetting() {
        if (!BackStack.isCurrentFragment(SLUtil.getActivity(), SettingFragment.NAME)) {
            showFragment(SettingFragment.newInstance());
        }
    }

    void showScanner() {
        SLUtil.getScannerUnion().scan();
    }

    void showCalc() {
        if (!BackStack.isCurrentFragment(SLUtil.getActivity(), CalcFragment.NAME)) {
            showFragment(CalcFragment.newInstance());
        }
    }

    void showPaging() {
        if (!BackStack.isCurrentFragment(SLUtil.getActivity(), PagingFragment.NAME)) {
            showFragment(PagingFragment.newInstance());
        }
    }

    void showPagingGoogle() {
        if (!BackStack.isCurrentFragment(SLUtil.getActivity(), PagingGoogleFragment.NAME)) {
            showFragment(PagingGoogleFragment.newInstance());
        }
    }

    void showContact() {
        if (!BackStack.isCurrentFragment(SLUtil.getActivity(), ContactFragment.NAME)) {
            showFragment(ContactFragment.newInstance());
        }
    }

}
