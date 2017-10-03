package com.jim.mpviews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DEV on 01.07.2017.
 */

public class MpMenuDialog extends RelativeLayout {

    private RecyclerView mpRecyclerView;
    private Context context;
    private List<String> arrayList;
    private MpMenuDialogAdapter menuDialogAdapter;

    public MpMenuDialog(Context context) {
        super(context);
        init(context);
    }

    public MpMenuDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpMenuDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MpMenuDialog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.mp_menu_dialog, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        arrayList = new ArrayList<>();
        mpRecyclerView = (RecyclerView) findViewById(R.id.mpRecyclerView);
        mpRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        setBackgroundResource(R.drawable.dialog_bg);
    }

    public void setAdapter() {
        menuDialogAdapter = new MpMenuDialogAdapter(context);
        mpRecyclerView.setAdapter(menuDialogAdapter);
    }

    public void setItems(ArrayList<String> items) {
        arrayList = items;
        menuDialogAdapter.notifyDataSetChanged();
    }

    public void setItems(String[] items) {
        arrayList.addAll(Arrays.asList(items));
    }

    public class MpMenuDialogAdapter extends RecyclerView.Adapter<MpMenuDialogAdapter.DialogViewHolder> {
        private Context context;
        private OnItemClickListener onItemClickListener;

        public MpMenuDialogAdapter(Context context) {
            this.context = context;
        }

        @Override
        public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mp_dialog_item, parent, false);
            return new DialogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DialogViewHolder holder, int position) {
            holder.textView.setText(arrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class DialogViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
            TextView textView;

            private DialogViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.mpMenuItem);
            }

            @Override
            public void onClick(View v) {
                onItemClickListener.setOnItemClick(v, getAdapterPosition());
            }
        }

        public void setOnItemClickListener (final OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    public interface OnItemClickListener {

        public void setOnItemClick(View view, int position);

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.arrayList = savedState.list;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.list = this.arrayList;
        return savedState;
    }

    static class SavedState extends BaseSavedState{
        List<String> list;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            in.readStringList(list);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeStringList(list);
            super.writeToParcel(out, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
