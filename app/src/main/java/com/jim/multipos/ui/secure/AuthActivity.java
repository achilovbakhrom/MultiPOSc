package com.jim.multipos.ui.secure;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jim.multipos.BuildConfig;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends BaseActivity {
    private HashMap<String, String> hashesWithSerial = (HashMap<String, String>) BuildConfig.LOCAL_SERIAL_HASH;

    @Inject
    PreferencesHelper preferencesHelper;

    @BindView(R.id.tvSerialNumber)
    TextView tvSerialNumber;

    @BindView(R.id.flConfirm)
    FrameLayout flConfirm;

    @BindView(R.id.flCall)
    FrameLayout flCall;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        ButterKnife.bind(this);


    }





}
