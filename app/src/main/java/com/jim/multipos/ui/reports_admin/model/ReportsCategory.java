package com.jim.multipos.ui.reports_admin.model;

public class ReportsCategory {
    String title;
    int id;

    public ReportsCategory(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}
