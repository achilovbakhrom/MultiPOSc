package com.jim.multipos.data.network.model;

public class SigninResponse {

    String status;
    int code;
    Response data;

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public Response getData() {
        return data;
    }

    public class Response{
        String access_token;
        long expires_in;
        long refresh_expires_in;
        String refresh_token;
        String token_type;
        String session_state;
        long expires_at;

        public String getAccess_token() {
            return access_token;
        }

        public long getExpires_in() {
            return expires_in;
        }

        public long getRefresh_expires_in() {
            return refresh_expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public String getSession_state() {
            return session_state;
        }

        public long getExpires_at() {
            return expires_at;
        }
    }
}
