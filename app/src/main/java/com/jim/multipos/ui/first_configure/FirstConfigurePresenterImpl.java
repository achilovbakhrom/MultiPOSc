package com.jim.multipos.ui.first_configure;

import android.content.Context;
import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import static com.jim.multipos.ui.first_configure.Constants.ADDRESS_DATA;
import static com.jim.multipos.ui.first_configure.Constants.ADDRESS_ERROR;
import static com.jim.multipos.ui.first_configure.Constants.ALIAS_DATA;
import static com.jim.multipos.ui.first_configure.Constants.ALIAS_ERROR;
import static com.jim.multipos.ui.first_configure.Constants.PASSWORD_DATA;
import static com.jim.multipos.ui.first_configure.Constants.PASSWORD_ERROR;
import static com.jim.multipos.ui.first_configure.Constants.PASSWORD_REPEAT_DATA;
import static com.jim.multipos.ui.first_configure.Constants.PASSWORD_REPEAT_ERROR;
import static com.jim.multipos.ui.first_configure.Constants.POS_ID_DATA;
import static com.jim.multipos.ui.first_configure.Constants.POS_ID_ERROR;

/**
 * Created by user on 07.10.17.
 */

public class FirstConfigurePresenterImpl extends BasePresenterImpl<FirstConfigureView> implements FirstConfigurePresenter {
    private Context context;
    private FirstConfigureView view;
    private boolean[] completedFragments;

    @Inject
    public FirstConfigurePresenterImpl(FirstConfigureView view) {
        super(view);
        this.view = view;
        this.context = (Context) view;
        completedFragments = new boolean[5];
    }

    @Override
    public void checkPosDetailsFragmentData(Map<String, String> data) {
        boolean hasError = false;
        boolean isPasswordEmpty = false;
        boolean isRepeatPasswordEmpty = false;
        String error = null;
        Map<String, String> errors = new HashMap<>();
        String password = data.get(PASSWORD_DATA);
        String passwordRepeat = data.get(PASSWORD_REPEAT_DATA);

        if (data.get(POS_ID_DATA).isEmpty()) {
            error = getString(R.string.enter_pos_id);
            hasError = true;
            errors.put(POS_ID_ERROR, error);
        }

        if (data.get(ALIAS_DATA).isEmpty()) {
            error = getString(R.string.enter_pos_alias);
            hasError = true;
            errors.put(ALIAS_ERROR, error);
        }

        if (data.get(ADDRESS_DATA).isEmpty()) {
            error = getString(R.string.enter_pos_address);
            hasError = true;
            errors.put(ADDRESS_ERROR, error);
        }

        if (password.isEmpty()) {
            //error = getString(R.string.enter_password);
            //hasError = true;
            isPasswordEmpty = true;
            //errors.put(PASSWORD_ERROR, error);
        }

        if (passwordRepeat.isEmpty()) {
            //error = getString(R.string.enter_repeat_password);
            //hasError = true;
            isRepeatPasswordEmpty = true;
            //errors.put(PASSWORD_REPEAT_ERROR, error);
        }

        //if (!isPasswordEmpty && !isRepeatPasswordEmpty) {
            int passwordCount = password.length();
            int repeatPasswordCount = passwordRepeat.length();
            boolean isPasswordDifferent = false;

            if (passwordCount != repeatPasswordCount) {
                error = getString(R.string.passwords_different);
                hasError = true;
                isPasswordDifferent = true;
                errors.put(PASSWORD_ERROR, error);
                errors.put(PASSWORD_REPEAT_ERROR, error);
            }

            if (!isPasswordDifferent) {
                if (passwordCount > 6) {
                    error = getString(R.string.password_should_note_be_more_than_6);
                    hasError = true;
                    errors.put(PASSWORD_ERROR, error);
                    errors.put(PASSWORD_REPEAT_ERROR, error);
                }
            }
        //}

        if (hasError) {
            view.showPosDetailsErrors(errors);
        } else {
            completedFragments[0] = true;
            openNextFragment();
        }
    }

    private void openNextFragment() {
        int nextPosition = -1;
        int notCompletedCount = 0;
        boolean isNextButton = false;

        for (int i = 0; i < completedFragments.length; i++) {
            if (completedFragments[i] != true) {
                nextPosition = i;
                break;
            }
        }

        for (int i = 0; i < completedFragments.length; i++) {
            if (completedFragments[i] != true) {
                notCompletedCount++;
            }
        }

        if (notCompletedCount == 0) {
            view.closeActivity();

            return;
        }

        if (notCompletedCount == 1) {
            isNextButton = false;
        } else {
            isNextButton = true;
        }

        if (nextPosition != -1) {
            view.replaceFragment(nextPosition, isNextButton);
        }
    }

    private String getString(int resId) {
        return context.getString(resId);
    }

    @Override
    public void getLeftSideFragmentData() {
        view.showLeftSideFragment(completedFragments);
    }
}
