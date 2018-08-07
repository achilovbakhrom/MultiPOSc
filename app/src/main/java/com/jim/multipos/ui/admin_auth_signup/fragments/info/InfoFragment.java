package com.jim.multipos.ui.admin_auth_signup.fragments.info;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_auth_events.InfoEvent;
import com.jim.multipos.utils.rxevents.admin_auth_events.OnBackEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class InfoFragment extends BaseFragment implements InfoView{

    @Inject
    RxBus bus;

    @Inject
    InfoPresenter presenter;

    @BindView(R.id.etFirstName)
    MpEditText etFirstName;
    @BindView(R.id.etLastName)
    MpEditText etLastName;
    @BindView(R.id.etDatePicker)
    MpEditText etDatePicker;
    @BindView(R.id.countryPicker)
    MPosSpinner countryPicker;
    @BindView(R.id.etPrimary_email)
    MpEditText etPrimary_email;
    @BindView(R.id.etPrimary_phone)
    MpEditText etPrimary_phone;
    @BindView(R.id.maleCheckBox)
    MpCheckbox maleCheckBox;
    @BindView(R.id.femaleCheckBox)
    MpCheckbox femaleCheckBox;


    String mail, password, country, gender = "male";
    Calendar myCalendar = Calendar.getInstance();

    @OnClick(R.id.nextBtn)
    public void onNextClick(View view){
        presenter.sendUserDetails(mail, password, etFirstName.getText().toString(),
                etLastName.getText().toString(), gender, etDatePicker.getText().toString(),
                country, etPrimary_email.getText().toString(), etPrimary_phone.getText().toString());
    }

    @OnClick(R.id.backBtn)
    public void onBackClick(View view){
        bus.send(new OnBackEvent());
    }

    @OnClick({R.id.maleCheckBox, R.id.femaleCheckBox})
    public void selectGender(View view){
        switch (view.getId()){
            case R.id.maleCheckBox:
                gender = "male";
                maleCheckBox.setChecked(true);
                femaleCheckBox.setChecked(false);
                break;
            case R.id.femaleCheckBox:
                gender = "female";
                maleCheckBox.setChecked(false);
                femaleCheckBox.setChecked(true);
                break;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mail = savedInstanceState.getString("mail");
        password = savedInstanceState.getString("pass");

        femaleCheckBox.setOnClickListener(this::selectGender);
        maleCheckBox.setOnClickListener(this::selectGender);

        String countries[] = {"Uzbekistan", "Russia", "USA"};
        countryPicker.setAdapter(countries);
        countryPicker.setItemSelectionListener((view, position) -> {
            switch (position) {
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
            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDatePicker.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onSuccess(String mail) {
        bus.send(new InfoEvent(mail));
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int error) {
        etPrimary_email.setError(getString(error));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
