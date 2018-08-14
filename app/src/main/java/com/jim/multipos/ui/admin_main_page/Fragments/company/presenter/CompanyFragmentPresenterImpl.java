package com.jim.multipos.ui.admin_main_page.fragments.company.presenter;

import com.jim.multipos.core.BasePresenterImpl;

public class CompanyFragmentPresenterImpl extends BasePresenterImpl<CompanyFragmentView> implements CompanyFragmentPresenter {

    CompanyFragmentView view;


    protected CompanyFragmentPresenterImpl(CompanyFragmentView companyFragmentView) {
        super(companyFragmentView);
        view = companyFragmentView;
    }

    @Override
    public void someMethod() {

    }
}
