package com.jim.multipos.data.network.model;

public class SignupResponse {
    public Signup data;
    public String status;
    public int code;

    public Signup getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }
}
