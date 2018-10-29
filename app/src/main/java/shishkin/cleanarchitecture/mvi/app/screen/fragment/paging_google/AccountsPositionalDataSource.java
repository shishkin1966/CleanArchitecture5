package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import android.support.annotation.NonNull;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.paging.AbsPositionalDataSource;

public class AccountsPositionalDataSource extends AbsPositionalDataSource<Account> {

    private int size = 400;
    private int y = 0;
    private int sleep = 200;

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Account> callback) {
        final List<Account> list = new ArrayList<>();
        for (int i = 0; i < params.pageSize; i++) {
            if (y >= size) {
                break;
            }
            final Account account = new Account();
            account.setFriendlyName("Счет " + y);
            account.setBalance(Double.valueOf("" + y));
            list.add(account);
            y++;
        }
        try {
            Thread.sleep(sleep);
            Thread.yield();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(getClass().getName(), e);
        }
        callback.onResult(list, 0);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Account> callback) {
        final List<Account> list = new ArrayList<>();
        for (int i = params.startPosition; i < params.startPosition + params.loadSize; i++) {
            if (y >= size) {
                break;
            }
            final Account account = new Account();
            account.setFriendlyName("Счет " + y);
            account.setBalance(Double.valueOf("" + y));
            list.add(account);
            y++;
        }
        try {
            Thread.sleep(sleep);
            Thread.yield();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(getClass().getName(), e);
        }
        callback.onResult(list);
    }

    @Override
    public void onInvalidated() {
        ApplicationUtils.runOnUiThread(() -> {
            final PagingGooglePresenter presenter = SLUtil.getPresenterUnion().getPresenter(PagingGooglePresenter.NAME);
            if (presenter != null) {
                presenter.hideProgressBar();
            }
            ApplicationUtils.showToast("Запрос прерван", Toast.LENGTH_SHORT, ApplicationUtils.MESSAGE_TYPE_INFO);
        });
    }
}
