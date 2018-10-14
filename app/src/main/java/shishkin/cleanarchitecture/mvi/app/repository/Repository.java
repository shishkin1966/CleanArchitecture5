package shishkin.cleanarchitecture.mvi.app.repository;

import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Маршрутизатор пользовательских запросов
 */

public class Repository {

    public static void addAccount(Account account, ResponseListener listener) {
        DbRepositoryProvider.addAccount(account, listener);
    }

    public static void getAccounts(ResponseListener listener) {
        DbRepositoryProvider.getAccounts(listener);
    }

    public static void getBalance(ResponseListener listener) {
        DbRepositoryProvider.getBalance(listener);
    }

    public static void getCurrency(ResponseListener listener) {
        DbRepositoryProvider.getCurrency(listener);
    }

    public static void getTicker(String listener) {
        NetRepositoryProvider.getTicker(listener);
    }

    public static void getValCurs(String listener, String date) {
        NetRepositoryProvider.getValCurs(listener, date);
    }

}
