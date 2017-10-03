package com.jim.multipos.ui.login;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by DEV on 04.08.2017.
 */

public class LoginPageImpl implements LoginPagePresenter {

    private LoginPageView view;

    @Override
    public void init(LoginPageView view) {
        this.view = view;
    }

    @Override
    public void loginEmp(String password) {

        //TODO where we check password
        view.loginEmployer(true);
    }

    @Override
    public void setItems(String[] emp, String[] role) {
        ArrayList<String> empList = new ArrayList<>();
        empList.addAll(Arrays.asList(emp));
        ArrayList<String> roleList = new ArrayList<>();
        roleList.addAll(Arrays.asList(role));
        ArrayList<String> photoList = new ArrayList<>();
        photoList.addAll(Arrays.asList(emp));
        view.setSpinnerItems(empList, roleList, photoList);
    }

    @Override
    public void loginAdmin(String password) {
        //TODO where we check password

        view.loginAdmin(true);
    }

    @Override
    public void setClockInHours() {
        DateFormat dateFormatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String time = dateFormatter.format(today);
        view.setClockInTime(time);
    }

    @Override
    public void setClockOutHours() {
        DateFormat dateFormatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String time = dateFormatter.format(today);
        view.setClockOutTime(time);
    }

}
