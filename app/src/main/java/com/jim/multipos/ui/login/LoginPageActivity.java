package com.jim.multipos.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpNumPad;
import com.jim.mpviews.MpSpinnerWithPhoto;
import com.jim.multipos.R;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.login.di.LoginPageComponent;
import com.jim.multipos.ui.login.di.LoginPageModule;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.utils.LoginAdminDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by DEV on 04.08.2017.
 */

public class LoginPageActivity extends BaseActivity implements HasComponent<LoginPageComponent>, LoginPageView {
    private LoginPageComponent loginPageComponent;
    @Inject
    LoginPagePresenter presenter;
    @BindView(R.id.btnClear)
    MpNumPad btnClear;
    @BindView(R.id.btnLogin)
    MpNumPad btnLogin;
    @BindView(R.id.btnLoginAdmin)
    MpButton btnLoginAdmin;
    @BindView(R.id.tvClockIn)
    TextView tvClockIn;
    @BindView(R.id.tvEmployerPassword)
    TextView tvEmployerPassword;
    @BindView(R.id.tvClockOut)
    TextView tvClockOut;
    @BindView(R.id.spEmployers)
    MpSpinnerWithPhoto spEmployers;
    @BindViews({R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine})
    List<MpNumPad> numPad;
    private char values[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private Timer timer;
    private String star = "*";
    private String password = "";
    private LoginAdminDialog dialog;
    @Inject
    PreferencesHelper preferencesHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_page);
        ButterKnife.bind(this);
        presenter.init(this);
        final long DELAY = 150;
        for (int i = 0; i < numPad.size(); i++) {
            final int count = i;
            RxView.clicks(numPad.get(i)).subscribe(aVoid -> {
                String text = tvEmployerPassword.getText().toString();
                text = text + values[count];
                tvEmployerPassword.setText(text);
                if (password.length() <= 5)
                    password = password + values[count];
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    tvEmployerPassword.setText(star);
                                    star = tvEmployerPassword.getText().toString();
                                    star = star + "*";
                                });
                            }
                        },
                        DELAY
                );

            });
        }
        String[] emp = {"Niko", "Don"};
        String[] role = {"Cacher", "Manager"};
        presenter.setItems(emp, role);

    }

    @OnClick(R.id.btnClear)
    public void onClear() {
        clear();
    }

    private void clear() {
        tvEmployerPassword.setText("");
        star = "*";
        password = "";
    }

    @OnClick(R.id.btnLogin)
    public void loginEmp() {
        presenter.loginEmp(password);
    }

    @OnClick(R.id.btnLoginAdmin)
    public void loginAdministrator() {
        dialog = new LoginAdminDialog(this);
        dialog.setOnOKClickListener(view -> {
            if (dialog.check())
                presenter.loginAdmin(dialog.getPassword());
        });
        dialog.show();
    }


    @Override
    protected void setupComponent(BaseAppComponent baseAppComponent) {
        loginPageComponent = baseAppComponent.plus(new LoginPageModule(this));
        loginPageComponent.inject(this);
    }

    @Override
    public LoginPageComponent getComponent() {
        return loginPageComponent;
    }

    @Override
    public void loginAdmin(boolean isRight) {
        if (isRight) {
            dialog.dismiss();
            Intent intent = new Intent(this, MainPosPageActivity.class);
            startActivity(intent);
        } else {
            dialog.setWrong();
        }
    }

    @Override
    public void loginEmployer(boolean isRight) {
        if (isRight) {
            presenter.setClockInHours();
            Intent intent = new Intent(this, MainPosPageActivity.class);
            startActivity(intent);
        } else {

        }
    }

    @Override
    public void setClockInTime(String time) {
        tvClockIn.setText(time);
    }

    @Override
    public void setClockOutTime(String time) {

    }

    @Override
    public void setSpinnerItems(ArrayList<String> empList, ArrayList<String> roleList, ArrayList<String> photoList) {
        spEmployers.setItems(empList, roleList, photoList);
        spEmployers.setAdapter();
    }

}
