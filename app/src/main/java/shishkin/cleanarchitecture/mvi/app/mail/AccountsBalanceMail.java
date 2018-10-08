package shishkin.cleanarchitecture.mvi.app.mail;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.observe.AccountsBalanceListener;
import shishkin.cleanarchitecture.mvi.sl.MailSubscriber;
import shishkin.cleanarchitecture.mvi.sl.mail.AbsMail;
import shishkin.cleanarchitecture.mvi.sl.mail.Mail;

public class AccountsBalanceMail extends AbsMail {

    private static final String NAME = AccountsBalanceMail.class.getName();

    private List<MviDao.Balance> list;

    public AccountsBalanceMail(final String address, List<MviDao.Balance> list) {
        super(address);

        this.list = list;
    }

    public List<MviDao.Balance> getAccountsBalance() {
        return list;
    }

    @Override
    public void read(MailSubscriber subscriber) {
        if (subscriber instanceof AccountsBalanceListener) {
            ((AccountsBalanceListener) subscriber).showAccountsBalance(list);
        }
    }

    @Override
    public Mail copy() {
        return new AccountsBalanceMail(getAddress(), getAccountsBalance());
    }

    @Override
    public String getName() {
        return NAME;
    }

}
