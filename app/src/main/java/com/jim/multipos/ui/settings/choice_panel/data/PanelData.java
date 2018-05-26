package com.jim.multipos.ui.settings.choice_panel.data;

public class PanelData {
    String title;
    String subTitle;

    public PanelData(String title, String sebTitle) {
        this.title = title;
        this.subTitle = sebTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
