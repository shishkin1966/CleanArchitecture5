package shishkin.cleanarchitecture.mvi.app.observe;

import java.util.List;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.BuildConfig;
import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.mail.AccountsBalanceMail;
import shishkin.cleanarchitecture.mvi.app.request.GetBalanceRequest;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts.AccountsPresenter;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu.SideMenuPresenter;
import shishkin.cleanarchitecture.mvi.common.InterruptByTime;
import shishkin.cleanarchitecture.mvi.common.InterruptListener;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.DbObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

public class AccountObserver implements DbObservableSubscriber, ResponseListener, InterruptListener {
    public static final String NAME = AccountObserver.class.getName();
    public static final String ACTION_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_CLICK";

    private static volatile AccountObserver sInstance;

    private static final String ACCOUNTS_MAILING_LIST = NAME + ".MAILING_LIST";
    private String[] mAccountsMailingList = {SideMenuPresenter.NAME, AccountsPresenter.NAME};
    private InterruptByTime interrupt = new InterruptByTime(this, TimeUnit.SECONDS.toMillis(5));

    public static void instantiate() {
        if (sInstance == null) {
            synchronized (AccountObserver.class) {
                if (sInstance == null) {
                    sInstance = new AccountObserver();
                }
            }
        }
    }

    public static AccountObserver getInstance() {
        if (sInstance == null) {
            instantiate();
        }
        return sInstance;
    }

    private AccountObserver() {
        SLUtil.getDbProvider().request(new GetBalanceRequest(this));

        SLUtil.getMessagerUnion().addMailingList(ACCOUNTS_MAILING_LIST, mAccountsMailingList);
    }

    @Override
    public List<String> getListenObjects() {
        return StringUtils.arrayToList(Account.TABLE);
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(DbObservable.NAME);
    }

    @Override
    public void onChange(Object object) {
        interrupt.up();
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                ObservableUnionImpl.NAME
        );
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getPasport() {
        return getName();
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public int getState() {
        return ViewStateObserver.STATE_RESUME;
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public void response(Result result) {
        if (!result.hasError()) {
            final List<MviDao.Balance> list = SafeUtils.cast(result.getData());
            ((ApplicationController) ApplicationController.getInstance()).updateWidget();
            SLUtil.addNotMandatoryMail(new AccountsBalanceMail(ACCOUNTS_MAILING_LIST, list));
        } else {
            SLUtil.getViewUnion().showMessage(new ShowMessageEvent(result.getErrorText(), ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

    @Override
    public void onInterrupt() {
        SLUtil.getDbProvider().request(new GetBalanceRequest(this));
    }
}
