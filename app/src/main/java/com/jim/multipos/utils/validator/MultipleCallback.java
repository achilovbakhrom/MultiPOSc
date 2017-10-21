package com.jim.multipos.utils.validator;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

/**
 * Created by user on 10.10.17.
 */

public class MultipleCallback implements IValidationCallback {
    private final boolean mFocusFirstFail = false;

    @Override
    public void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations, List<View> passedValidations) {
        if (!failedValidations.isEmpty()) {
            for (FormValidator.ValidationFail view : failedValidations) {
                if (view.view instanceof TextView) {
                    ((TextView) view.view).setError(view.message);
                }
            }
            FormValidator.ValidationFail firstFail = failedValidations.get(0);
            if (mFocusFirstFail) {
                firstFail.view.requestFocus();
            }
        }
    }
}