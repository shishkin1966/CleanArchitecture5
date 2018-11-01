package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.datasource.AbsPositionalDataSource;

public class AccountsPagedDataSource extends AbsPositionalDataSource<Account> {

    public static final String NAME = AccountsPagedDataSource.class.getName();

    private int size = 0;
    private int y = 0;
    private long sleep = TimeUnit.SECONDS.toMillis(1);

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Account> callback) {
        size = 400;
        final PagingGooglePresenter presenter = SLUtil.getPresenterUnion().getPresenter(PagingGooglePresenter.NAME);
        if (presenter != null) {
            ApplicationUtils.runOnUiThread(() -> presenter.showProgressBar());
        }
        final List<Account> list = new ArrayList<>();
        for (int i = 0; i < params.pageSize; i++) {
            if (y >= size) {
                break;
            }
            final Account account = new Account();
            account.setFriendlyName("Счет " + (y + 1));
            account.setBalance(Double.valueOf(y + 1));
            list.add(account);
            y++;
        }
        try {
            Thread.sleep(sleep / 3);
            Thread.yield();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(getClass().getName(), e);
        }
        if (presenter != null) {
            ApplicationUtils.runOnUiThread(() -> presenter.hideProgressBar());
        }
        callback.onResult(list, 0);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Account> callback) {
        final PagingGooglePresenter presenter = SLUtil.getPresenterUnion().getPresenter(PagingGooglePresenter.NAME);
        if (presenter != null) {
            ApplicationUtils.runOnUiThread(() -> presenter.showProgressBar());
        }
        final List<Account> list = new ArrayList<>();
        for (int i = params.startPosition; i < params.startPosition + params.loadSize; i++) {
            if (y >= size) {
                break;
            }
            final Account account = new Account();
            account.setFriendlyName("Счет " + (y + 1));
            account.setBalance(Double.valueOf(y + 1));
            list.add(account);
            y++;
        }
        try {
            Thread.sleep(sleep);
            Thread.yield();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(getClass().getName(), e);
        }
        if (presenter != null) {
            ApplicationUtils.runOnUiThread(() -> presenter.hideProgressBar());
        }
        callback.onResult(list);
    }

    @Override
    public void onInvalidated() {
        super.onInvalidated();

        ApplicationUtils.runOnUiThread(() -> {
            ApplicationUtils.showToast("Источник данных остановлен", Toast.LENGTH_SHORT, ApplicationUtils.MESSAGE_TYPE_INFO);
        });
    }
}
