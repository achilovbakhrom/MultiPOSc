package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.jim.multipos.R;

public class EntitiesDialog extends Dialog {

    TextView tvProduct;
    TextView tvProductClass;

    public EntitiesDialog(@NonNull Context context, OnDialogItemClickListener listener) {
        super(context);

        getLayoutInflater().inflate(R.layout.admin_entity_menu_layout, null);

    }

    public interface OnDialogItemClickListener {
        void onProduct();

        void onProductClass();

        void onDiscount();

        void onServiceFee();
    }
}
