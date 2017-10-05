package com.jim.mpviews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jim.mpviews.utils.StateSaver;
import com.jim.mpviews.utils.Test;
import com.jim.mpviews.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by developer on 20.05.2017.
 */

public class MpSpinner extends RelativeLayout {
    Spinner spinner;
    ImageView imageView;
    LinearLayout linearLayout;
    ArrayAdapter adapter;
    List<String> arrayList;
    List<String> list;
    private String withDefaultValue = null;
    private boolean isEnable = true;

    public MpSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public MpSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.withDefaultValue = ss.stringValue;
        this.isEnable = ss.boolValue;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stringValue = this.withDefaultValue;
        ss.boolValue = this.isEnable;

        return ss;
    }


    static class SavedState extends BaseSavedState {
        String stringValue;
        boolean boolValue;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stringValue = in.readString();
            this.boolValue = in.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.stringValue);
            out.writeInt(boolValue ? 1 : 0);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }


    public MpSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    Context context;

    private void init(Context context, AttributeSet attrs) {
        Log.d(Test.TAG, "init");
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.mp_spinner, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpSpinner);
        spinner = (Spinner) findViewById(R.id.spProductQuantity);
        imageView = (ImageView) findViewById(R.id.mpSpIcon);
        linearLayout = (LinearLayout) findViewById(R.id.mpSpinnerLayout);
        arrayList = new ArrayList<>();
        list = new ArrayList<>();
        linearLayout.setBackgroundResource(attributeArray.getResourceId(R.styleable.MpSpinner_sp_bg, R.drawable.edit_text_bg));
        imageView.setImageResource(attributeArray.getResourceId(R.styleable.MpSpinner_img_color, R.drawable.triangle));
        setOnClickListener(view -> {
            if (isEnable)
                spinner.performClick();
        });

    }

    public void setItems(List<String> items) {
        arrayList.clear();
        arrayList.addAll(items);
    }

    public void setItems(List<String> items, List<String> items2) {
        arrayList = items;
        list = items2;
    }

    public void setItems(String[] items) {
        arrayList.addAll(Arrays.asList(items));
    }


    public void setAdapter() {
        if (adapter == null) {
            adapter = new MpSpinnerAdapter(context, R.layout.mp_spinner_adapter);
            spinner.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void setAdapter(ArrayAdapter adapter) {
        this.adapter = adapter;
        spinner.setAdapter(adapter);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

    private String unPickValue = null;

    public void setUnPickValue(String unPickValue) {
        this.unPickValue = unPickValue;
        arrayList.add(0, unPickValue);

    }

    public void setWithDefaultValueForFirstTime(String defaultValue) {
        withDefaultValue = defaultValue;
        arrayList.add(0, "");
    }

    private String key = null;

    public void setState(String key) {
        int position = StateSaver.getInstance(getContext()).getStateSaver().getInt(key, 0);
        this.key = key;
        Log.d(Test.TAG, "state :" + String.valueOf(position));
        if (spinner.getAdapter().getCount() < position) {
            unselected = false;
            spinner.setSelection(0);
        } else {
            unselected = false;
            spinner.setSelection(position);
        }

    }

    MpSpinner.setOnItemClickListener onItemClickListener;
    boolean firstChangedItem = true;

    public void setOnItemSelectedListener(MpSpinner.setOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (firstChangedItem) firstChangedItem = false;
                else {
                    Log.d(Test.TAG, "item selected: " + String.valueOf(i));
                    if (key != null) if (!key.isEmpty())
                        StateSaver.getInstance(getContext()).getStateSaver().edit().putInt(key, i).apply();
                    MpSpinner.this.onItemClickListener.onItemSelected(adapterView, view, i, l);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public interface setOnItemClickListener {
        void onItemSelected(AdapterView<?> adapterView, View view, int i, long l);
    }

    private boolean unselected = true;

    public void setSelection(int position) {
        spinner.post(() -> spinner.setSelection(position));
    }

    public int selectedItemPosition() {
        int selected = spinner.getSelectedItemPosition();
        return selected;
    }
    public Object getSelectedItem() {
        return spinner.getSelectedItem();
    }


    public void setSpinnerEnabled(boolean enabled) {
        spinner.setEnabled(enabled);
        spinner.setClickable(enabled);
        linearLayout.setClickable(enabled);
        linearLayout.setEnabled(enabled);
        isEnable = enabled;
    }

    private class MpSpinnerAdapter extends ArrayAdapter {
        LayoutInflater inflater;
        Context context;
        TextView textView;

        public MpSpinnerAdapter(Context context, int resouceId) {
            super(context, resouceId, arrayList);
            this.context = context;
            inflater = ((Activity) context).getLayoutInflater();
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, parent);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            return getForDefault(pos, parent);
        }

        public View getForDefault(int position, ViewGroup parent) {
            textView = new TextView(context);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.ten_dp));
            if (withDefaultValue != null && unselected) {
                textView.setText(withDefaultValue);
                textView.setTextColor(Color.parseColor("#b9b9b9"));
                unselected = false;
            } else if (arrayList.size() != 0) {
                textView.setText(arrayList.get(position));
                textView.setTextColor(Color.parseColor("#212121"));
            } else {
                textView.setText("");
            }

            /*//TODO change color
            if (!isEnable) {
                textView.setTextColor(Color.parseColor("#e0dbdb"));
            }*/

            return textView;
        }

        public View getCustomView(int position, ViewGroup parent) {
            if (arrayList.get(position).equals(""))
                return inflater.inflate(R.layout.emp, parent, false);
            View mySpinner = inflater.inflate(R.layout.mp_spinner_adapter, parent, false);
            TextView textView = (TextView) mySpinner.findViewById(R.id.spinnerTextView);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.ten_dp));
            textView.setText(arrayList.get(position));
            mySpinner.setAlpha(0.1f);
            mySpinner.animate().alpha(1f).setDuration(400).start();
            return mySpinner;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }


// implementation of mpSpinner
//        mpSpinner1 = (MpSpinner) findViewById(R.id.mpSpinner1);
//        mpSpinner1.setUnPickValue("default");
//        mpSpinner1.setWithDefaultValueForFirstTime("(set value)");
//        mpSpinner1.setItems(new String[]{"Uzs","Dollar"});
//        mpSpinner1.setAdapter();
////        mpSpinner1.setState(MpSpinnerState.GROUP_CURRENCY);
//        mpSpinner1.setOnItemSelectedListener(new MpSpinner.setOnItemClickListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d(Test.TAG, "onItemClick: "+mpSpinner1.selectedItemPosition());
//            }
//        });

}
