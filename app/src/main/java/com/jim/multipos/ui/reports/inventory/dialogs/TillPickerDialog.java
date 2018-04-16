package com.jim.multipos.ui.reports.inventory.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jim.mpviews.RecyclerViewWithMaxHeight;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.reports.inventory.adapter.TillPickerAdapter;
import com.jim.multipos.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.03.2018.
 */

public class TillPickerDialog extends Dialog {


    @BindView(R.id.rvTills)
    RecyclerViewWithMaxHeight rvTills;

    public TillPickerDialog(@NonNull Context context, DatabaseManager databaseManager, OnTillPickedListener listener) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.choose_till_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        List<Till> tills = databaseManager.getAllClosedTills().blockingGet();
        rvTills.setMaxHeight(400);
        rvTills.setLayoutManager(new LinearLayoutManager(context));
        TillPickerAdapter adapter = new TillPickerAdapter(tills, (till, position) -> {
            listener.pickedTill(till);
            dismiss();
        });
        rvTills.setAdapter(adapter);
    }

    public interface OnTillPickedListener {
        void pickedTill(Till till);
    }
}
