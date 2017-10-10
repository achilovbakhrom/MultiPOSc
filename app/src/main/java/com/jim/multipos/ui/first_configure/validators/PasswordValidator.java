package com.jim.multipos.ui.first_configure.validators;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.validators.BaseValidator;

/**
 * Created by user on 10.10.17.
 */

public class PasswordValidator extends BaseValidator<String[]> {
    @Override
    public boolean validate(Annotation annotation, String[] input) {

        if (input[0].equals(input[1])) {
            return true;
        }

        return false;
    }
}
