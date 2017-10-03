package com.jim.multipos.ui.first_configure.fragments;

import com.jim.multipos.data.db.model.Account;

import java.util.List;

/**
 * Created by user on 01.08.17.
 */

public interface AccountFragmentView {
    void showAccountNameError(String error);
    void showAccountType(String[] accountType);
    void showAccountCirculation(String[] accountCirculation);
    void showAdapter(List<Account> accounts, String[] accountType, String[] accountCirculation);
    void showAddedAccount();
    void clearViews();
    void openNextFragment();
    void showAccountEmptyError(String error);
    void accountRemoved(int position);
}
