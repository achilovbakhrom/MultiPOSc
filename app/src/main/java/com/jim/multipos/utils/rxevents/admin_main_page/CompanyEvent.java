package com.jim.multipos.utils.rxevents.admin_main_page;

public class CompanyEvent {
    String companyName;
    String companyDescription;
    String companyID;
    String companyAddress;
    String companyIPCODE;

    public CompanyEvent(String companyName, String companyDescription, String companyID, String companyAddress, String companyIPCODE) {
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.companyID = companyID;
        this.companyAddress = companyAddress;
        this.companyIPCODE = companyIPCODE;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyIPCODE() {
        return companyIPCODE;
    }
}
