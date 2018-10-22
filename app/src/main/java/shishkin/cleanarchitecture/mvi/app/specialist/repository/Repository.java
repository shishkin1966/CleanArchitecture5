package shishkin.cleanarchitecture.mvi.app.specialist.repository;

import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Маршрутизатор пользовательских запросов
 */
public interface Repository {

    void getAccounts(ResponseListener listener);

    void getBalance(ResponseListener listener);

    void getCurrency(ResponseListener listener);

    void getTicker(String listener);

    void getValCurs(String listener, String date);

    void getPagingAccounts(String listener);

    void cancelRequests(String listener);

    void addAccount(ResponseListener listener, Account account);
}
