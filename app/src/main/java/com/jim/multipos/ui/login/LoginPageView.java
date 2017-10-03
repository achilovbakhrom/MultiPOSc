package com.jim.multipos.ui.login;

import java.util.ArrayList;

/**
 * Created by DEV on 04.08.2017.
 */

public interface LoginPageView {
    void loginAdmin(boolean isRight);
    void loginEmployer(boolean isRight);
    void setClockInTime(String s);
    void setClockOutTime(String time);
    void setSpinnerItems(ArrayList<String> empList, ArrayList<String> roleList, ArrayList<String> photoList);
}
