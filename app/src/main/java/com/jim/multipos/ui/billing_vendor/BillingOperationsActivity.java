package com.jim.multipos.ui.billing_vendor;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.mpviews.utils.Test;
import com.jim.multipos.R;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.utils.DateIntervalPicker;
import com.jim.multipos.utils.TestUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.CONSIGNMENT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.CONSIGNMENT_TYPE;

/**
 * Created by developer on 29.11.2017.
 */

public class BillingOperationsActivity extends SimpleActivity {
    public static final String VENDOR_EXTRA_ID = "vendor_id";
    public static final String VENDOR_DEBT = "vendor_debt";
    SimpleDateFormat simpleDateFormat ;
    Calendar fromDate;
    Calendar toDate;
    boolean fromDateCurrent  = false, toDateCurrent = false;

    @Inject
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        BillingOperationFragment billingOperationFragment = new BillingOperationFragment();
        billingOperationFragment.setVendorId(getIntent().getExtras().getLong(VENDOR_EXTRA_ID));
        billingOperationFragment.setTotalDebt(getIntent().getExtras().getDouble(VENDOR_DEBT));
        addFragment(billingOperationFragment);
        Calendar from = (Calendar) Calendar.getInstance().clone();
        Calendar to = (Calendar) Calendar.getInstance().clone();
        from.set(Calendar.MONTH, from.get(Calendar.MONTH) - 1);

        from.set(Calendar.MONTH,from.get(Calendar.MONTH)-1);
        toolbar.setDataIntervalPicker(from, to, new MpToolbar.DataIntervalCallbackToToolbar() {
            @Override
            public void onDataIntervalPickerPressed() {
                DateIntervalPicker dateIntervalPicker = new DateIntervalPicker(BillingOperationsActivity.this,fromDate,toDate, new DateIntervalPicker.CallbackIntervalPicker() {
                    @Override
                    public void dateIntervalPicked(Calendar fromDate, Calendar toDate) {
                        BillingOperationsActivity.this.fromDate = fromDate;
                        BillingOperationsActivity.this.toDate = toDate;
                        fromDateCurrent = true;
                        toDateCurrent = true;
                        updateCurrentDate();
                        billingOperationFragment.dateIntervalPicked(fromDate,toDate);
                        toolbar.changeToCloseImgIntervalPick();
                    }

                    @Override
                    public void datePicked(Calendar pickedDate) {
                        BillingOperationsActivity.this.fromDate = pickedDate;
                        BillingOperationsActivity.this.toDate = null;
                        fromDateCurrent = true;
                        toDateCurrent = false;
                        updateCurrentDate();
                        billingOperationFragment.datePicked(pickedDate);
                        toolbar.changeToCloseImgIntervalPick();

                    }
                });
                dateIntervalPicker.show();
            }

            @Override
            public void clearInterval() {
                BillingOperationsActivity.this.fromDate = null;
                BillingOperationsActivity.this.toDate = null;
                toolbar.setDatePickerIntervalText(getString(R.string.select_date));
                billingOperationFragment.clearInterval();
                toolbar.changeToCalendarImgIntervalPick();

            }
        });
        toolbar.setDatePickerIntervalText(getString(R.string.select_date));
    }


    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.WITH_CALENDAR_TYPE;
    }

    private void updateCurrentDate(){
        String date = "";
        if(fromDateCurrent  && !toDateCurrent){
            String format = simpleDateFormat.format(fromDate.getTime());
            date =  format.substring(0, 1).toUpperCase() + format.substring(1);
        }else if(fromDateCurrent  && toDateCurrent ){
            long indecatorTo = toDate.get(Calendar.YEAR) * 365 + toDate.get(Calendar.MONTH) * 30 + toDate.get(Calendar.DAY_OF_MONTH);
            long indecatorFrom = fromDate.get(Calendar.YEAR) * 365 + fromDate.get(Calendar.MONTH) * 30 + fromDate.get(Calendar.DAY_OF_MONTH);
            if(indecatorTo == indecatorFrom){
                String format = simpleDateFormat.format(fromDate.getTime());
                date =  format.substring(0, 1).toUpperCase() + format.substring(1);
            }else {
                String format = simpleDateFormat.format(fromDate.getTime());
                String format1 = simpleDateFormat.format(toDate.getTime());
                date = format.substring(0, 1).toUpperCase() + format.substring(1) + " - " + format1.substring(0, 1).toUpperCase() + format1.substring(1);
            }

        }
        toolbar.setDatePickerIntervalText(date);

    }



    public void openConsignment(Long consignmentId, int type) {
        Intent intent = new Intent(this, ConsignmentActivity.class);
        intent.putExtra(CONSIGNMENT_ID, consignmentId);
        intent.putExtra(CONSIGNMENT_TYPE, type);
        startActivity(intent);
    }
}
