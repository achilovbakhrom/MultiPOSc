package com.jim.multipos.ui.consignment_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment;
import com.jim.multipos.utils.DateIntervalPicker;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public class ConsignmentListActivity extends SimpleActivity implements ConsignmentListActivityView {

    protected static final int WITH_TOOLBAR = 1;
    SimpleDateFormat simpleDateFormat;
    Calendar fromDate;
    Calendar toDate;
    boolean fromDateCurrent = false, toDateCurrent = false;

    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Long vendorId = bundle.getLong(VENDOR_ID);
            Bundle bundle1 = new Bundle();
            bundle1.putLong(VENDOR_ID, vendorId);
            ConsignmentListFragment fragment = new ConsignmentListFragment();
            fragment.setArguments(bundle1);
            addFragment(fragment);
//            simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
//            Calendar from = (Calendar) Calendar.getInstance().clone();
//            Calendar to = (Calendar) Calendar.getInstance().clone();
//            from.set(Calendar.MONTH, from.get(Calendar.MONTH) - 1);
//
//            toolbar.getBarcodeView().setVisibility(View.GONE);
//
//            toolbar.setDataIntervalPicker(from, to, new MpToolbar.DataIntervalCallbackToToolbar() {
//                @Override
//                public void onDataIntervalPickerPressed() {
//                    DateIntervalPicker dateIntervalPicker = new DateIntervalPicker(ConsignmentListActivity.this, fromDate, toDate, new DateIntervalPicker.CallbackIntervalPicker() {
//                        @Override
//                        public void dateIntervalPicked(Calendar fromDate, Calendar toDate) {
//                            ConsignmentListActivity.this.fromDate = fromDate;
//                            ConsignmentListActivity.this.toDate = toDate;
//                            fromDateCurrent = true;
//                            toDateCurrent = true;
//                            updateCurrentDate();
//                            fragment.dateIntervalPicked(fromDate, toDate);
//                            toolbar.changeToCloseImgIntervalPick();
//                        }
//
//
//                    });
//                    dateIntervalPicker.show();
//                }
//
//                @Override
//                public void clearInterval() {
//                    ConsignmentListActivity.this.fromDate = null;
//                    ConsignmentListActivity.this.toDate = null;
//                    toolbar.setDatePickerIntervalText(getString(R.string.select_date));
//                    fragment.clearInterval();
//                    toolbar.changeToCalendarImgIntervalPick();
//
//                }
//            });
//            toolbar.setDatePickerIntervalText(getString(R.string.select_date));
        }

    }

    private void updateCurrentDate() {
        String date = "";
        if (fromDateCurrent && !toDateCurrent) {
            String format = simpleDateFormat.format(fromDate.getTime());
            date = format.substring(0, 1).toUpperCase() + format.substring(1);
        } else if (fromDateCurrent && toDateCurrent) {
            long indicatorTo = toDate.get(Calendar.YEAR) * 365 + toDate.get(Calendar.MONTH) * 30 + toDate.get(Calendar.DAY_OF_MONTH);
            long indicatorFrom = fromDate.get(Calendar.YEAR) * 365 + fromDate.get(Calendar.MONTH) * 30 + fromDate.get(Calendar.DAY_OF_MONTH);
            if (indicatorTo == indicatorFrom) {
                String format = simpleDateFormat.format(fromDate.getTime());
                date = format.substring(0, 1).toUpperCase() + format.substring(1);
            } else {
                String format = simpleDateFormat.format(fromDate.getTime());
                String format1 = simpleDateFormat.format(toDate.getTime());
                date = format.substring(0, 1).toUpperCase() + format.substring(1) + " - " + format1.substring(0, 1).toUpperCase() + format1.substring(1);
            }

        }
        toolbar.setDatePickerIntervalText(date);

    }

    public void openConsignment(Long consignmentId, Integer consignmentType) {
//        Intent intent = new Intent(this, ConsignmentActivity.class);
//        intent.putExtra(INVOICE_TYPE, consignmentType);
//        startActivity(intent);
    }
}
