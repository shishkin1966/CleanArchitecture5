package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import shishkin.cleanarchitecture.mvi.sl.ui.IActivity;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface MainView extends IActivity {
    void hideSideMenu();

    void onConnect();

    void onDisconnect();
}
