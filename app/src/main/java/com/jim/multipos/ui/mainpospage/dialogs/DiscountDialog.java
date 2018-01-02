package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.ui.mainpospage.adapter.DiscountAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 09.11.2017.
 */

public class DiscountDialog extends Dialog implements DiscountAdapter.OnClickListener {

    @BindView(R.id.tvCaption)
    TextView tvCaption;
    @BindView(R.id.rvDiscounts)
    RecyclerView recyclerView;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnAdd)
    MpButton btnAdd;
    private DiscountAdapter adapter;
    private List<Discount> discounts;
    private String caption;
    private DatabaseManager databaseManager;

    public DiscountDialog(@NonNull Context context, DatabaseManager databaseManager) {
        super(context);
        this.databaseManager = databaseManager;
        View dialogView = getLayoutInflater().inflate(R.layout.discount_dialog_new, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        discounts = databaseManager.getAllDiscounts().blockingGet();
        if (caption != null) {
            tvCaption.setText(caption);
        }
        String[] discountTypes = context.getResources().getStringArray(R.array.discount_amount_types_abr);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DiscountAdapter(getContext(), this, discounts, discountTypes);
        recyclerView.setAdapter(adapter);

        RxView.clicks(btnBack).subscribe(o -> dismiss());

        RxView.clicks(btnAdd).subscribe(o -> {
            AddDiscountDialog dialog = new AddDiscountDialog(getContext(), databaseManager, 120000, Discount.ORDER);
            dialog.show();
            dismiss();
        });
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public void onItemClicked(Discount discount) {
        //TODO
        dismiss();
    }
}
