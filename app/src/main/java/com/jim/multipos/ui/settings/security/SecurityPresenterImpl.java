package com.jim.multipos.ui.settings.security;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class SecurityPresenterImpl extends BasePresenterImpl<SecurityView> implements SecurityPresenter {

    @Inject
    protected SecurityPresenterImpl(SecurityView securityView) {
        super(securityView);
    }
}
