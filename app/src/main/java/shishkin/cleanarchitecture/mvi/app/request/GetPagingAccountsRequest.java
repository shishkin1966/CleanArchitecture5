package shishkin.cleanarchitecture.mvi.app.request;

import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.mail.ResultMail;
import shishkin.cleanarchitecture.mvi.sl.request.AbsResultMailRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Rank;

public class GetPagingAccountsRequest extends AbsResultMailRequest {

    public static final String NAME = GetPagingAccountsRequest.class.getName();
    private int y = 1;

    public GetPagingAccountsRequest(String listener) {
        super(listener);

        setRank(Rank.LOW_RANK);
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void run() {
        int cnt = 10;
        int sleep = 200;
        try {
            for (int i = 0; i < 10; i++) {
                if (!validate()) {
                    ApplicationUtils.runOnUiThread(() -> {
                        ApplicationUtils.showToast("Запрос прерван", Toast.LENGTH_SHORT, ApplicationUtils.MESSAGE_TYPE_INFO);
                    });
                    return;
                }

                final List<Account> list = new ArrayList<>();
                for (int ii = 0; ii < cnt; ii++) {
                    final Account account = new Account();
                    account.setFriendlyName("Счет " + y);
                    account.setBalance(Double.valueOf("" + y));
                    list.add(account);
                    y++;
                }
                final Result<List<Account>> result = new Result<>(list).setOrder(i);
                if (i == 9) {
                    result.setOrder(Result.LAST);
                }
                if (validate()) {
                    ApplicationUtils.runOnUiThread(() -> {
                        SLUtil.addMail(new ResultMail(getOwnerName(), result));
                    });
                }
                Thread.sleep(sleep);
                Thread.yield();

                if (cnt == 10) {
                    cnt = 20;
                    sleep = 400;
                } else if (cnt == 20) {
                    cnt = 100;
                    sleep = 2000;
                }
            }
        } catch (Exception e) {
            final Result<List<Account>> result = new Result<>().setError(NAME, e);
            if (validate()) {
                ApplicationUtils.runOnUiThread(() -> {
                    SLUtil.addMail(new ResultMail(getOwnerName(), result));
                });
            }
        }
    }
}
