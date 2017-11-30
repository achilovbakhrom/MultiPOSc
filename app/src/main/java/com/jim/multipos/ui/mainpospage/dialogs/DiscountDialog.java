package com.jim.multipos.ui.mainpospage.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.ui.mainpospage.adapter.DiscountAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Portable-Acer on 09.11.2017.
 */

public class DiscountDialog extends DialogFragment implements DiscountAdapter.OnClickListener {
    public interface OnDialogListener {
        List<Discount> getDiscounts();
        void addDiscount(double amount, String description, String amountType);
    }

    @BindView(R.id.tvCaption)
    TextView tvCaption;
    @BindView(R.id.rvDiscounts)
    RecyclerView recyclerView;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnAdd)
    MpButton btnAdd;
    private Unbinder unbinder;
    private DiscountAdapter adapter;
    private OnDialogListener listener;
    private List<Discount> discounts;
    private String[] discountTypes;
    private String caption;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discount_dialog_new, container, false);
        getDialog().getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);

        unbinder = ButterKnife.bind(this, view);

        if (caption != null) {
            tvCaption.setText(caption);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DiscountAdapter(getContext(), this, discounts, discountTypes);
        recyclerView.setAdapter(adapter);

        RxView.clicks(btnBack).subscribe(o -> dismiss());

        RxView.clicks(btnAdd).subscribe(o -> {
            AddDiscountDialog dialog = new AddDiscountDialog();
            dialog.setOnDiscountDialogListener(new AddDiscountDialog.OnDiscountDialogListener() {
                @Override
                public void dismiss() {
                    adapter.setItems(listener.getDiscounts());
                }

                @Override
                public void addDiscount(double amount, String description, String amountType) {
                    listener.addDiscount(amount, description, amountType);
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "AddDiscountDialog");
        });

        return view;
    }

    public void setDiscounts(List<Discount> discounts, String[] discountTypes) {
        this.discounts = discounts;
        this.discountTypes = discountTypes;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setOnDialogListener(OnDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();

        super.onDestroyView();
    }

    @Override
    public void onItemClicked(Discount discount) {
        //TODO
        dismiss();
    }
}
