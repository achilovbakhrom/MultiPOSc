package com.jim.multipos.ui.first_configure.presenters;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.ui.first_configure.common.BaseFragmentPresenterFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.AccountFragmentView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 05.08.17.
 */

public interface AccountFragmentPresenter extends BaseFragmentPresenterFirstConfig<AccountFragmentView> {
    boolean isCompleteData();

    List<Account> getData();

    void addAccount(String name, int type, int circulation);

    void showAccountType();

    void showAccountCirculation();

    void setAdapterData();

    void openNextFragment(HashMap<String, String> data);

    void removeAccount(int position);
}
