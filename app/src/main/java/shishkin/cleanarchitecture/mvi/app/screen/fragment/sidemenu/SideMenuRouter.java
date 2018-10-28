package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.model.BaseModelRouter;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation.CalcFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.contact.ContactFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies.DigitalCurrenciesFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.map.MapFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.paging.PagingFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.setting.SettingFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs.ValCursFragment;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

class SideMenuRouter extends BaseModelRouter {

    SideMenuRouter(AbsModel model) {
        super(model);
    }

    void showDigitalCurrencies() {
        showFragment(DigitalCurrenciesFragment.newInstance());
    }

    void showValCurs() {
        showFragment(ValCursFragment.newInstance());
    }

    void showMap() {
        showFragment(MapFragment.newInstance());
    }

    void showSetting() {
        showFragment(SettingFragment.newInstance());
    }

    void showScanner() {
        SLUtil.getScannerUnion().scan();
    }

    void showCalc() {
        showFragment(CalcFragment.newInstance());
    }

    void showPaging() {
        showFragment(PagingFragment.newInstance());
    }

    void showContact() {
        showFragment(ContactFragment.newInstance());
    }

}
