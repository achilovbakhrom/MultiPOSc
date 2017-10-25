package com.jim.multipos.ui.lock_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpNumPad;
import com.jim.multipos.R;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.utils.WarningDialog;

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

public class LockScreenActivity extends BaseActivity implements LockScreenView {
    @Inject
    LockScreenPresenter presenter;
    @BindView(R.id.btnClear)
    MpNumPad btnClear;
    @BindView(R.id.btnLogin)
    MpNumPad btnLogin;
    @BindView(R.id.tvEmployerPassword)
    TextView tvEmployerPassword;
    @BindViews({R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine})
    List<MpNumPad> numPad;
    private char values[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private Timer timer;
    private String star = "*";
    private String password = "";
    private final static int EMPTY = 0;
    private final static int WRONG_PASS = 1;
    private WarningDialog dialog;
    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        ButterKnife.bind(this);
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
    public void onLogin() {
        presenter.checkPassword(password);
    }

    @Override
    public void successCheck() {
        Intent intent = new Intent(this, MainPosPageActivity.class);
        startActivity(intent);
        clear();
    }

    @Override
    public void setError(int state) {
        switch (state) {
            case EMPTY:
                dialog = new WarningDialog(this);
                dialog.onlyText(true);
                dialog.setWarningText("Please, enter the password");
                dialog.setOnYesClickListener(view -> {
                    clear();
                    dialog.dismiss();
                });
                dialog.show();
                break;
            case WRONG_PASS:
                dialog = new WarningDialog(this);
                dialog.onlyText(true);
                dialog.setWarningText("Wrong password");
                dialog.setOnYesClickListener(view -> {
                    clear();
                    dialog.dismiss();
                });
                dialog.show();
                break;
        }
    }
}
