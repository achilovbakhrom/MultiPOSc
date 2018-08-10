package com.jim.multipos.ui.admin_main_page.fragments.company.model;

public class CompanyModel {

    String companyName;
    String companyDescription;
    String comapnyLogoUrl;

    public CompanyModel(String companyName, String companyDescription, String comapnyLogoUrl) {
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.comapnyLogoUrl = comapnyLogoUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public String getComapnyLogoUrl() {
        return comapnyLogoUrl;
    }
}
