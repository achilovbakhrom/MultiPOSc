package com.jim.multipos.ui.settings.choice_panel.data;

import lombok.Data;

@Data
public class PanelData {
    String title;
    String subTitle;

    public PanelData(String title, String sebTitle) {
        this.title = title;
        this.subTitle = sebTitle;
    }
}
