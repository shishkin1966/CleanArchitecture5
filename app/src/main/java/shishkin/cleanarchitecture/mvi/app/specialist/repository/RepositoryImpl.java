package shishkin.cleanarchitecture.mvi.app.specialist.repository;

import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public class RepositoryImpl extends AbsSpecialist implements Repository {

    public static final String NAME = RepositoryImpl.class.getName();

    @Override
    public void addAccount(ResponseListener listener, Account account) {
        DbRepositoryProvider.addAccount(listener, account);
    }

    @Override
    public void getAccounts(ResponseListener listener) {
        DbRepositoryProvider.getAccounts(listener);
    }

    @Override
    public void getBalance(ResponseListener listener) {
        DbRepositoryProvider.getBalance(listener);
    }

    @Override
    public void getCurrency(ResponseListener listener) {
        DbRepositoryProvider.getCurrency(listener);
    }

    @Override
    public void getTicker(String listener) {
        NetRepositoryProvider.getTicker(listener);
    }

    @Override
    public void getValCurs(String listener, String date) {
        NetRepositoryProvider.getValCurs(listener, date);
    }

    @Override
    public void getPagingAccounts(String listener) {
        DbRepositoryProvider.getPagingAccounts(listener);
    }

    @Override
    public void cancelRequests(String listener) {
        DbRepositoryProvider.cancelRequests(listener);
        NetRepositoryProvider.cancelRequests(listener);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (Repository.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
