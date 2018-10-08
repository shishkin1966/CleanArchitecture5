package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;


import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

public class SideMenuModel extends AbsModel {

    public SideMenuModel(SideMenuView view) {
        super(view);

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
}
