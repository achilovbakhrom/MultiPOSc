package com.jim.multipos.ui.first_configure_last.adapter;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by bakhrom on 10/17/17.
 */

public class FirstConfigureListItem {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private int state;
    @Getter @Setter
    private boolean selected = false;
}
