package com.jim.mpviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jim.mpviews.utils.Utils;

/**
 * Created by bakhrom on 10/16/17.
 */

public class MPosSpinner extends FrameLayout {

    private ItemSelectionListener listener;
    private boolean firstListner = false;
    public MPosSpinner(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MPosSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MPosSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MPosSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.edit_text_bg);
        AppCompatSpinner spinner = new AppCompatSpinner(context);
        spinner.setId(R.id.spinner);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.topMargin = Utils.dpToPx(2);
        lp.bottomMargin = Utils.dpToPx(2);
        lp.leftMargin = Utils.dpToPx(4);
        lp.rightMargin = Utils.dpToPx(4);
        lp.gravity = Gravity.CENTER_VERTICAL;
        spinner.setLayoutParams(lp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!firstListner)
                if (listener != null) {
                    listener.onItemSelected(view, i);
                }
                else {
                    firstListner = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addView(spinner);

        ImageView imageView = new ImageView(context);
        imageView.setId(R.id.spinner_arrow);
        LayoutParams ivLp = new LayoutParams(Utils.dpToPx(20), Utils.dpToPx(20));
        ivLp.rightMargin = Utils.dpToPx(4);
        ivLp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(ivLp);
        addView(imageView);
    }

    public void setItemSelectionListener(ItemSelectionListener listener) {
        this.listener = listener;
    }
    public void setItemSelectionListenerWithPos(ItemSelectionListener listener) {
        this.listener = listener;
    }
    public void setArrowResource(int id) {
        ((ImageView) findViewById(R.id.spinner_arrow)).setImageResource(id);
    }
    public void setOnClickListner(AdapterView.OnItemSelectedListener onClickListner){
        ((Spinner)findViewById(R.id.spinner)).setOnItemSelectedListener(onClickListner);
    }
    public void setAdapter(String[] items, String defaultItemName) {
        String[] temp = new String[items.length + 1];
        int size = items.length + 1;
        for(int i = 0; i < size; i++) {
            if (i == 0) {
                temp[i] = defaultItemName;
            } else {
                temp[i] = items[i-1];
            }
        }
        setAdapter(items);
    }

    public void setAdapter(BaseAdapter adapter) {
        ((AppCompatSpinner)findViewById(R.id.spinner)).setAdapter(adapter);
    }

    public void setAdapter(String[] items) {
        ((AppCompatSpinner)findViewById(R.id.spinner))
                .setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items));
    }

    public void setArrowTint(int color) {
        ((ImageView) findViewById(R.id.spinner_arrow)).setColorFilter(color);
    }

    public interface ItemSelectionListener {
        void onItemSelected(View view, int position);
    }

    public int getSelectedPosition() {
        return ((AppCompatSpinner)findViewById(R.id.spinner)).getSelectedItemPosition();
    }

    public void setSelection(int position) {
        ((AppCompatSpinner) findViewById(R.id.spinner)).setSelection(position);
    }
    public void setSelectedPosition(int position){
        ((AppCompatSpinner)findViewById(R.id.spinner)).setSelection(position);
    }
    public void setSelectedPosition(int position, boolean b){
        ((AppCompatSpinner)findViewById(R.id.spinner)).setSelection(position,b);
    }
}
