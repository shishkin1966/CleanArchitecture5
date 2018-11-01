package shishkin.cleanarchitecture.mvi.app.request;

import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.paged_load.PagedPresenter;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.paginator.Paginator;
import shishkin.cleanarchitecture.mvi.sl.request.AbsPaginatorRequest;
import shishkin.cleanarchitecture.mvi.sl.request.PaginatorRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.task.RequestThreadPoolExecutor;

public class AccountsPaginatorRequest extends AbsPaginatorRequest<List<Account>> {

    public static final String NAME = AccountsPaginatorRequest.class.getName();

    public AccountsPaginatorRequest(Paginator paginator, int currentPosition, int currentPageSize) {
        super(paginator, currentPosition, currentPageSize);
    }

    @Override
    public void run() {
        if (isCancelled()) {
            ApplicationUtils.runOnUiThread(() -> ApplicationUtils.showToast("Запрос прерван", Toast.LENGTH_SHORT, ApplicationUtils.MESSAGE_TYPE_INFO));
            return;
        }

        if (getCurrentPosition() >= 400) {
            setData(new ArrayList<>());
            response();
            return;
        }

        try {
            ApplicationUtils.runOnUiThread(() -> {
                final PagedPresenter presenter = SLUtil.getPresenterUnion().getPresenter(PagedPresenter.NAME);
                if (presenter != null) {
                    presenter.showProgressBar();
                }
            });

            final List<Account> list = new ArrayList<>();
            for (int i = 0; i < getCurrentPageSize(); i++) {
                if (getCurrentPosition() + i >= 400) {
                    break;
                }
                final Account account = new Account();
                account.setFriendlyName("Счет " + (getCurrentPosition() + i + 1));
                account.setBalance(Double.valueOf(getCurrentPosition() + i + 1));
                list.add(account);
            }
            Thread.sleep(getCurrentPageSize() * 4);
            Thread.yield();
            setData(list);
            ApplicationUtils.runOnUiThread(() -> {
                final PagedPresenter presenter = SLUtil.getPresenterUnion().getPresenter(PagedPresenter.NAME);
                if (presenter != null) {
                    presenter.hideProgressBar();
                }
            });
        } catch (Exception e) {
            setError(new ExtError().addError(NAME, e));
        }
        if (!isCancelled()) {
            response();
        } else {
            ApplicationUtils.runOnUiThread(() -> ApplicationUtils.showToast("Запрос прерван", Toast.LENGTH_SHORT, ApplicationUtils.MESSAGE_TYPE_INFO));
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getAction(Request oldRequest) {
        final PaginatorRequest request = (PaginatorRequest) oldRequest;
        if (request.getCurrentPosition() == getCurrentPosition()) {
            return RequestThreadPoolExecutor.ACTION_IGNORE;
        }
        return RequestThreadPoolExecutor.ACTION_NOTHING;
    }
}
