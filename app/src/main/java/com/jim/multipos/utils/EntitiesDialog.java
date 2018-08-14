package com.jim.multipos.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jim.multipos.R;

public class EntitiesDialog extends Dialog {

    TextView tvProduct, tvProductClass, tvDiscount, tvServiceFee;
    int defaultColor;

    @SuppressLint("ClickableViewAccessibility")
    public EntitiesDialog(@NonNull Context context, View view, OnDialogItemClickListener listener) {
        super(context);

        View dialog = getLayoutInflater().inflate(R.layout.admin_entity_menu_layout, null);

        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        int[] coords = {0, 0};
        view.getLocationOnScreen(coords);
        wlp.y = (coords[1] + view.getHeight()) - 26;
        wlp.x = coords[0] - 28;
        wlp.width = (int) (view.getWidth() * 3.5);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        setContentView(dialog);
        tvProduct = findViewById(R.id.tvProduct);
        tvProductClass = findViewById(R.id.tvProductClass);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvServiceFee = findViewById(R.id.tvServiceFee);

        defaultColor = tvProduct.getCurrentTextColor();
        tvProduct.setOnClickListener(v -> listener.onProduct());
        tvProduct.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tvProduct.setTextColor(Color.WHITE);
                    tvProduct.setBackgroundColor(Color.parseColor("#57A1D1"));
                    return true;
                case MotionEvent.ACTION_UP:
                    tvProduct.setTextColor(defaultColor);
                    tvProduct.setBackgroundColor(Color.WHITE);
                    listener.onProduct();
                    dismiss();
                    return false;
            }
            return false;
        });

        tvProductClass.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tvProductClass.setTextColor(Color.WHITE);
                    tvProductClass.setBackgroundColor(Color.parseColor("#57A1D1"));
                    return true;
                case MotionEvent.ACTION_UP:
                    tvProductClass.setTextColor(defaultColor);
                    tvProductClass.setBackgroundColor(Color.WHITE);
                    listener.onProductClass();
                    dismiss();
                    return false;
            }
            return false;
        });

        tvDiscount.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tvDiscount.setTextColor(Color.WHITE);
                    tvDiscount.setBackgroundColor(Color.parseColor("#57A1D1"));
                    return true;
                case MotionEvent.ACTION_UP:
                    tvDiscount.setTextColor(defaultColor);
                    tvDiscount.setBackgroundColor(Color.WHITE);
                    listener.onDiscount();
                    dismiss();
                    return false;
            }
            return false;
        });

        tvServiceFee.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tvServiceFee.setTextColor(Color.WHITE);
                    tvServiceFee.setBackgroundColor(Color.parseColor("#57A1D1"));
                    return true;
                case MotionEvent.ACTION_UP:
                    tvServiceFee.setTextColor(defaultColor);
                    tvServiceFee.setBackgroundColor(Color.WHITE);
                    listener.onServiceFee();
                    dismiss();
                    return false;
            }
            return false;
        });

    }

    public interface OnDialogItemClickListener {
        void onProduct();

        void onProductClass();

        void onDiscount();

        void onServiceFee();
    }
}
