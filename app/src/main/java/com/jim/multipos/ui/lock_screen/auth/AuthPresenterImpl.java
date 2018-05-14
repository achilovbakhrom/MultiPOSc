package com.jim.multipos.ui.lock_screen.auth;

import android.os.Bundle;

import com.jim.multipos.BuildConfig;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.utils.SecurityTools;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

public class AuthPresenterImpl extends BasePresenterImpl<AuthView> implements AuthPresenter {

    private HashMap<String, String> hashesWithSerial = (HashMap<String, String>) BuildConfig.LOCAL_SERIAL_HASH;


    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    protected AuthPresenterImpl(AuthView authView) {
        super(authView);
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);

        if(preferencesHelper.getSerialValue().equals("")){
            Random rnd = new Random();
            rnd.setSeed(System.currentTimeMillis());
            int randInt = rnd.nextInt(300);
            int count = 0;
            for (Map.Entry<String,String> set:hashesWithSerial.entrySet()){
                if(count == randInt){
                    preferencesHelper.setSerialValue(set.getKey());
                    view.setSerialNumberToView(preferencesHelper.getSerialValue());
                }
                count ++;
            }
        }else {
            view.setSerialNumberToView(preferencesHelper.getSerialValue());
        }
    }

    @Override
    public void checkRegistrationToken(String token) {
        if(hashesWithSerial.get(preferencesHelper.getSerialValue()).equals(SecurityTools.hashPassword(preferencesHelper.getSerialValue()+token))){
            preferencesHelper.setRegistrationToken(token);
            view.valid();
        }else {
            view.invalidToken();
        }
    }
}
