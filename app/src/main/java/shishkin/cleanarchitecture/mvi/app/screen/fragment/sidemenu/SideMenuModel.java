package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;


import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

public class SideMenuModel extends AbsModel {

    SideMenuModel(SideMenuView view) {
        super(view);

        setInteractor(new SideMenuInteractor());
        setRouter(new SideMenuRouter(this));
        setPresenter(new SideMenuPresenter(this));
    }

    @Override
    public SideMenuPresenter getPresenter() {
        return super.getPresenter();
    }

    @Override
    public SideMenuFragment getView() {
        return super.getView();
    }

    @Override
    public SideMenuRouter getRouter() {
        return super.getRouter();
    }

    @Override
    public SideMenuInteractor getInteractor() {
        return super.getInteractor();
    }
}
