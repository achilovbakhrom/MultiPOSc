package com.jim.multipos.data.network.model;

public class Signin {
    String grant_type;
    String client_id;
    String client_secret;
    String username;
    String password;

    public Signin(String grant_type, String client_id, String client_secret, String username, String password) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.username = username;
        this.password = password;
    }
}
