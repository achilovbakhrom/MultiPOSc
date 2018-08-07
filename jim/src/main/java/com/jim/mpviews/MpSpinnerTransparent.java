package com.jim.mpviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jim.mpviews.utils.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by developer on 20.05.2017.
 */

public class MpSpinnerTransparent extends RelativeLayout {
    MpSpinnerWithPhoto.setOnItemClickListener onItemClickListener;
    boolean firstChangedItem = true;
    private Spinner spinner;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private MpSpinnerAdapter adapter;
    private ArrayList<String> nameList;
    private ArrayList<String> roleList;
    private ArrayList<String> photoList;
    private Context context;
    private String key = null;

    public MpSpinnerTransparent(Context context) {
        super(context);
        init(context, null);
    }

    public MpSpinnerTransparent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpSpinnerTransparent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpSpinnerTransparent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Log.d(Test.TAG, "init");
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.transparent_spinner_layout, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpSpinner);
        spinner = findViewById(R.id.spProductQuantity);
        imageView = findViewById(R.id.mpSpIcon);
        linearLayout = findViewById(R.id.mpSpinnerLayout);
        nameList = new ArrayList<>();
        roleList = new ArrayList<>();
        photoList = new ArrayList<>();
//        linearLayout.setBackgroundResource(attributeArray.getResourceId(R.styleable.MpSpinner_sp_bg, R.drawable.edit_text_bg));
        imageView.setImageResource(attributeArray.getResourceId(R.styleable.MpSpinner_img_color, R.drawable.triangle));
        setOnClickListener(view -> spinner.performClick());

    }

    public void setItems(ArrayList<String> name, ArrayList<String> position, ArrayList<String> photoID) {
        nameList = name;
        roleList = position;
        photoList = photoID;
    }

    public void setItems(String[] name, String[] position, String[] photoID) {
        nameList.addAll(Arrays.asList(name));
        roleList.addAll(Arrays.asList(position));
        photoList.addAll(Arrays.asList(photoID));
    }

    public void setAdapter() {
        adapter = new MpSpinnerAdapter(context);
        spinner.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(MpSpinnerWithPhoto.setOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (firstChangedItem) firstChangedItem = false;
                else {
                    Log.d(Test.TAG, "item selected: " + String.valueOf(i));
                    MpSpinnerTransparent.this.onItemClickListener.onItemSelected(adapterView, view, i, l);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public int selectedItem() {
        int selected = spinner.getSelectedItemPosition();
        if (selected < 0) selected = -1;
        return selected;
    }

    public interface setOnItemClickListener {
        void onItemSelected(AdapterView<?> adapterView, View view, int i, long l);
    }

    private class MpSpinnerAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Context context;

        public MpSpinnerAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return nameList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.mp_spinner_employer, null);
            TextView empName = convertView.findViewById(R.id.tvEmpName);
            empName.setText(nameList.get(position));
            empName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.ten_dp));
            TextView empRole = convertView.findViewById(R.id.tvEmpRole);
            empRole.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.seven_dp));
            empRole.setText(roleList.get(position));
            convertView.setAlpha(0.1f);
            convertView.animate().alpha(1f).setDuration(400).start();
            return convertView;
        }

    }

}