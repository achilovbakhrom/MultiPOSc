package com.jim.multipos.ui.registration;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by developer on 31.08.2017.
 */

public class TestClass implements Serializable {
    @SerializedName("field")
    String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
