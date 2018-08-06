package com.jim.multipos.ui.admin_auth_signup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpRoundedImageView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.network.MultiPosApiService;
import com.jim.multipos.data.network.model.Signup;
import com.jim.multipos.data.network.model.SignupResponse;
import com.jim.multipos.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminAuthSignupActivity extends BaseActivity implements AdminAuthSignupActivityView {

    @BindView(R.id.generalSection)
    LinearLayout generalSection;
    @BindView(R.id.infoSection)
    LinearLayout infoSection;
    @BindView(R.id.confirmationSection)
    LinearLayout confirmationSection;
    @BindView(R.id.backBtn)
    Button backBtn;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.generalCircle)
    View generalCircle;
    @BindView(R.id.gen2infoLine)
    View gen2infoLine;
    @BindView(R.id.infoCircle)
    View infoCircle;
    @BindView(R.id.info2completeLine)
    View info2completeLine;
    @BindView(R.id.completeCircle)
    View completeCircle;
    @BindView(R.id.etLogin)
    MpEditText etLogin;
    @BindView(R.id.etPassword)
    MpEditText etPassword;
    @BindView(R.id.etConfirmPassword)
    MpEditText etConfirmPassword;
    @BindView(R.id.etFirstName)
    MpEditText etFirstName;
    @BindView(R.id.etLastName)
    MpEditText etLastName;
    @BindView(R.id.maleCheckBox)
    MpCheckbox maleCheckBox;
    @BindView(R.id.femaleCheckBox)
    MpCheckbox femaleCheckBox;
    @BindView(R.id.etDatePicker)
    MpEditText etDatePicker;
    @BindView(R.id.countryPicker)
    MPosSpinner countryPicker;
    @BindView(R.id.ivAvatar)
    MpRoundedImageView ivAvatar;
    @BindView(R.id.etPrimary_phone)
    MpEditText etPrimary_phone;
    @BindView(R.id.etPrimary_email)
    MpEditText etPrimary_email;
    @BindView(R.id.confirmation)
    MpEditText confirmation;
    @BindView(R.id.tvGeneral)
    TextView tvGeneral;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    String country = "Uzbekistan";
    String email;
    Calendar myCalendar = Calendar.getInstance();
    int color;

    @Inject
    MultiPosApiService service;
    @Inject
    AdminAuthSignupActivityPresenter presenter;

    int state = 1;

    @OnClick({R.id.backBtn, R.id.nextBtn})
    public void onBtnPressed(View view){
        switch (view.getId()){
            case R.id.backBtn:
                state = 1;
                changeVisibility(state);
                break;
            case R.id.nextBtn:
                if(nextBtn.getText().equals(getString(R.string.confirm)))
                    confirm();
                else {
                    state++;
                    changeVisibility(state);
                }
                break;
        }
    }

    public void changeVisibility(int val){
        if(val == 1){
            generalSection.setVisibility(View.VISIBLE);
            infoSection.setVisibility(View.GONE);
            confirmationSection.setVisibility(View.GONE);
            generalCircle.setBackground(getDrawable(R.drawable.reg_current_circle));
            gen2infoLine.setBackgroundColor(getColor(R.color.colorGreySecondaryDark));
            infoCircle.setBackground(getDrawable(R.drawable.reg_not_completed_circle));
            info2completeLine.setBackgroundColor(getColor(R.color.colorGreySecondaryDark));
            completeCircle.setBackground(getDrawable(R.drawable.reg_not_completed_circle));
            tvGeneral.setTextColor(color);
            tvInfo.setTextColor(getColor(R.color.colorGreySecondaryDark));
            tvConfirm.setTextColor(getColor(R.color.colorGreySecondaryDark));

        }else if(val == 2){
                boolean isValid = true;
                if(!Utils.isEmailValid(etLogin.getText().toString())) {
                    etLogin.setError(getString(R.string.incorrect_email));
                    isValid = false;
                }
                if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    etConfirmPassword.setError(getString(R.string.password_dont_match));
                    isValid = false;
                }
                if(etLogin.getText().toString().equals("")) {
                    etLogin.setError(getString(R.string.required_field));
                    isValid = false;
                }
                if(etPassword.getText().toString().equals("")) {
                    etPassword.setError(getString(R.string.required_field));
                    isValid = false;
                }
                if(etConfirmPassword.getText().toString().equals("")) {
                    etConfirmPassword.setError(getString(R.string.required_field));
                    isValid = false;
                }

                if(isValid) {
                    generalSection.setVisibility(View.GONE);
                    infoSection.setVisibility(View.VISIBLE);
                    confirmationSection.setVisibility(View.GONE);
                    generalCircle.setBackground(getDrawable(R.drawable.reg_completed_circle));
                    gen2infoLine.setBackgroundColor(getColor(R.color.colorCircleBlue));
                    infoCircle.setBackground(getDrawable(R.drawable.reg_current_circle));
                    info2completeLine.setBackgroundColor(getColor(R.color.colorGreySecondaryDark));
                    completeCircle.setBackground(getDrawable(R.drawable.reg_not_completed_circle));
                    tvInfo.setTextColor(color);
                    tvGeneral.setTextColor(getColor(R.color.colorGreySecondaryDark));
                    tvConfirm.setTextColor(getColor(R.color.colorGreySecondaryDark));
                }else state--;

        }else
            if(val == 3 && Utils.isEmailValid(etPrimary_email.getText().toString())){
                boolean isValid = true;
                if(etFirstName.getText().toString().equals("")){
                    isValid = false;
                    etFirstName.setError(getString(R.string.required_field));
                }
                if(etLastName.getText().toString().equals("")){
                    isValid = false;
                    etLastName.setError(getString(R.string.required_field));
                }
                if(etDatePicker.getText().toString().equals("")){
                    isValid = false;
                    etDatePicker.setError(getString(R.string.required_field));
                }
                if(etPrimary_email.getText().toString().equals("")){
                    isValid = false;
                    etPrimary_email.setError(getString(R.string.required_field));
                }
                if(etPrimary_phone.getText().toString().equals("")){
                    isValid = false;
                    etPrimary_phone.setError(getString(R.string.required_field));
                }
                if(isValid) {
                    generalSection.setVisibility(View.GONE);
                    infoSection.setVisibility(View.GONE);
                    confirmationSection.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    nextBtn.setText(R.string.confirm);
                    infoCircle.setBackground(getDrawable(R.drawable.reg_completed_circle));
                    info2completeLine.setBackgroundColor(getColor(R.color.colorCircleBlue));
                    completeCircle.setBackground(getDrawable(R.drawable.reg_current_circle));
                    tvConfirm.setTextColor(color);
                    tvGeneral.setTextColor(getColor(R.color.colorGreySecondaryDark));
                    tvInfo.setTextColor(getColor(R.color.colorGreySecondaryDark));
                    sendData();
                }else state--;
        }else {
            etPrimary_email.setError("Incorrect email");
            state--;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_registration_layout);
        ButterKnife.bind(this);

        color = tvGeneral.getCurrentTextColor();

        maleCheckBox.setCheckedChangeListener(isChecked -> {
            femaleCheckBox.setChecked(!isChecked);
        });
        femaleCheckBox.setCheckedChangeListener(isChecked -> {
            maleCheckBox.setChecked(!isChecked);
        });

        String countries[] = {"Uzbekistan", "Russia", "USA"};
        countryPicker.setAdapter(countries);
        countryPicker.setItemSelectionListener((view, position) -> {
            switch (position){
                case 0:
                    country = countries[0];
                    break;
                case 1:
                    country = countries[1];
                    break;
                case 2:
                    country = countries[2];
                    break;
            }
        });
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        etDatePicker.setKeyListener(null);
        etDatePicker.setOnClickListener(v -> {
            new DatePickerDialog(AdminAuthSignupActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDatePicker.setText(sdf.format(myCalendar.getTime()));
    }

    void confirm(){
        if(confirmation.getText().toString().equals(""))
            confirmation.setError(getString(R.string.confirmation_error));
        else
            presenter.confirm(email, Integer.parseInt(confirmation.getText().toString()));
    }

    void sendData(){
        String gender;
        if(femaleCheckBox.isChecked())
            gender = "female";
        else gender = "male";

        presenter.sendAdminDetails(new Signup(etLogin.getText().toString(), etPassword.getText().toString(), etFirstName.getText().toString(), etLastName.getText().toString(),
                gender, etPrimary_phone.getText().toString(), etPrimary_email.getText().toString(), country, etDatePicker.getText().toString()));
    }

    @Override
    public void onError(String error) {
        Toast.makeText(AdminAuthSignupActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(SignupResponse response) {
        email = response.data.getMail();
    }

    @Override
    public void onConfirmationSuccess() {
        Toast.makeText(AdminAuthSignupActivity.this, "Success", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConfirmationIncorrectCode() {

    }

    @Override
    public void onConfirmationError(String error) {
        Toast.makeText(AdminAuthSignupActivity.this, "Conf Error" + error, Toast.LENGTH_LONG).show();

    }
}
