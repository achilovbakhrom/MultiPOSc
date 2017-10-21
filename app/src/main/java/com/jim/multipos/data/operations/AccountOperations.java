package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.Account;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by user on 17.08.17.
 */

public interface AccountOperations {
    Observable<Account> addAccount(Account account);
    Observable<Boolean> addAccounts(List<Account> accounts);
    Observable<List<Account>> getAllAccounts();
    Observable<Boolean> removeAccount(Account account);
    Observable<Boolean> removeAllAccounts();
    Observable<Boolean> isAccountNameExists(String name);
    List<Account> getAccounts();
}
