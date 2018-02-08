package com.jim.multipos.utils.managers;

import android.content.Context;
import android.view.KeyEvent;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.mainpospage.view.OrderListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sirojiddin on 30.01.2018.
 */

public class BarcodeScannerManager {

    private Context context;
    public static final String DEVICE_NAME = "SCANNER SCANNER";
    private List<BaseView> views;

    public BarcodeScannerManager(Context context) {
        this.context = context;
        views = new ArrayList<>();
    }

    public void onKeyDown(KeyEvent event, String barcode) {
        if (event.getDevice().getName().equals(DEVICE_NAME)) {
            if (views.size() != 0)
                views.get(0).onBarcodeScan(barcode);
        }
    }

    public void initView(BaseView baseView) {
        views.add(0, baseView);
    }

    public void removeView(BaseView baseView) {
        views.remove(baseView);
    }

}
