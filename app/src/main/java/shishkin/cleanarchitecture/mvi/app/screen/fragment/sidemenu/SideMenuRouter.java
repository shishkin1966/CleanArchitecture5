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

public class SideMenuRouter extends BaseModelRouter {

    public SideMenuRouter(AbsModel model) {
        super(model);
    }

    public void showDigitalCurrencies() {
        showFragment(DigitalCurrenciesFragment.newInstance());
    }

    public void showValCurs() {
        showFragment(ValCursFragment.newInstance());
    }

    public void showMap() {
        showFragment(MapFragment.newInstance());
    }

    public void showSetting() {
        showFragment(SettingFragment.newInstance());
    }

    public void showScanner() {
        SLUtil.getScannerUnion().scan();
    }

    public void showCalc() {
        showFragment(CalcFragment.newInstance());
    }

    public void showPaging() {
        showFragment(PagingFragment.newInstance());
    }

    public void showContact() {
        showFragment(ContactFragment.newInstance());
    }

}
