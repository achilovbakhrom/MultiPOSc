package com.jim.multipos.ui.admin_auth_signup.fragments.info;

import com.jim.multipos.core.Presenter;

public interface InfoPresenter extends Presenter {
    void sendUserDetails(String email, String password, String fn, String ln, String gender, String dob, String country, String primary_email, String primary_phone);
}
