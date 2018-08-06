package com.jim.multipos.data.network.model;

public class Signup {
    String mail;
    String password;
    String first_name;
    String last_name;
    String gender;
    String primary_phone;
    String primary_email;
    String country;
    String date_of_birth;

    public Signup(String mail, String password, String first_name, String last_name, String gender, String primary_phone, String primary_email, String country, String date_of_birth) {
        this.mail = mail;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.primary_phone = primary_phone;
        this.primary_email = primary_email;
        this.country = country;
        this.date_of_birth = date_of_birth;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getGender() {
        return gender;
    }

    public String getPrimary_phone() {
        return primary_phone;
    }

    public String getPrimary_email() {
        return primary_email;
    }

    public String getCountry() {
        return country;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }
}
