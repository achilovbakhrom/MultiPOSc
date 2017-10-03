package com.jim.multipos.ui.registration.presenters;


import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.registration.fragments.RegistrationFragmentView;

/**
 * Created by DEV on 26.07.2017.
 */

public interface RegistrationPresenter extends BaseFragmentPresenter<RegistrationFragmentView> {
    void setItems(String[] items);
    void displayFragment(String name, String address, String email, String code);
    void popBackStack();
    void wrapData();
    void setRecyclerViewItems(String s, String toString);
}
