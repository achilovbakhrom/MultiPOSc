package com.jim.multipos.ui.first_configure.presenters;

import android.content.Context;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.data.operations.StockOperations;
import com.jim.multipos.data.prefs.AppPreferencesHelper;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragmentView;

import java.util.HashMap;

/**
 * Created by user on 01.08.17.
 */

public class PosFragmentPresenterImpl implements PosFragmentPresenter {
    private PosDetailsFragmentView view;
    private Context context;
    private HashMap<String, String> data;
    PreferencesHelper preferencesHelper;

    public PosFragmentPresenterImpl(Context context) {
        this.context = context;
        preferencesHelper = new AppPreferencesHelper(context);
    }

    @Override
    public void init(PosDetailsFragmentView view) {
        this.view = view;
    }


    @Override
    public boolean isCompleteData(HashMap<String, String> data) {
        if (data.get("posId").isEmpty()) {
            return false;
        }

        if (data.get("alias").isEmpty()) {
            return false;
        }

        if (data.get("address").isEmpty()) {
            return false;
        }

        if (data.get("password").isEmpty()) {
            return false;
        }

        if (data.get("repeatPassword").isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean checkData(HashMap<String, String> data) {
        boolean hasError = false;

        String posId = data.get("posId");
        String alias = data.get("alias");
        String address = data.get("address");
        String password = data.get("password");
        String repeatPassword = data.get("repeatPassword");

        if (posId.isEmpty()) {
            view.showPosIdError(getString(R.string.enter_pos_id));
            hasError = true;
        }

        if (alias.isEmpty()) {
            view.showAliasError(getString(R.string.enter_alias));
            hasError = true;
        }

        if (address.isEmpty()) {
            view.showAddressError(getString(R.string.enter_address));
            hasError = true;
        }

        if (password.isEmpty() && repeatPassword.isEmpty()) {
            view.showPasswordError(getString(R.string.enter_password));
            view.showRepeatPasswordError(getString(R.string.enter_repeat_password));
            hasError = true;
        } else {
            if (password.isEmpty()) {
                view.showPasswordError(getString(R.string.enter_password));
                hasError = true;
            }

            if (repeatPassword.isEmpty()) {
                view.showRepeatPasswordError(getString(R.string.enter_repeat_password));
                hasError = true;
            }

            if (!password.equals(repeatPassword)) {
                view.showPasswordError(getString(R.string.passwords_different));
                view.showRepeatPasswordError(getString(R.string.passwords_different));
                hasError = true;
            }

            if (password.length() < 12 || repeatPassword.length() < 12) {
                view.showPasswordError(getString(R.string.password_length_not_less_than_12));
                view.showRepeatPasswordError(getString(R.string.password_length_not_less_than_12));
                hasError = true;
            }
        }

        return hasError;
    }

    @Override
    public void openNextFragment(HashMap<String, String> data) {
        boolean hasError = checkData(data);

        if (!hasError) {
            this.data = data;
            view.openNextFragment();
        }
    }

    private String getString(int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public void saveData() {
        preferencesHelper.setPosDetailPosId(data.get("posId"));
        preferencesHelper.setPosDetailAlias(data.get("alias"));
        preferencesHelper.setPosDetailAddress(data.get("address"));
        preferencesHelper.setPosDetailPassword(data.get("password"));
    }
}
