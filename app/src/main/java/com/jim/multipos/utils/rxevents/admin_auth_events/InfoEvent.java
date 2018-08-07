package com.jim.multipos.utils.rxevents.admin_auth_events;

public class InfoEvent {
    String firstName;
    String lastName;
    String gender;
    String dob;
    String country;
    String primary_email;
    String primary_phone;

    public InfoEvent(String firstName, String lastName, String gender, String dob, String country, String primary_email, String primary_phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.country = country;
        this.primary_email = primary_email;
        this.primary_phone = primary_phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getCountry() {
        return country;
    }

    public String getPrimary_email() {
        return primary_email;
    }

    public String getPrimary_phone() {
        return primary_phone;
    }
}
