package com.jim.multipos.ui.first_configure.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.unit.Unit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 08.08.17.
 */

public class UnitAdapterOld extends RecyclerView.Adapter<UnitAdapterOld.ViewHolder> {
    private String[] unitsProperties;
    private int whichAdapter;
    private OnClick onClickCallback;
    private List<Unit> units;

    public UnitAdapterOld(List<Unit> units, String[] unitsProperties, int whichAdapter, OnClick onClickCallback) {
        this.units = units;
        this.unitsProperties = unitsProperties;
        this.whichAdapter = whichAdapter;
        this.onClickCallback = onClickCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StringBuilder str = new StringBuilder();
        Unit unit = units.get(position);

        str.append(unit.getName());
        str.append(" (");
        str.append(unit.getAbbr());
        str.append(")");

        holder.tvUnit.setText(str.toString());
        holder.tvUnitProperty.setText(unitsProperties[position]);

        if (unit.getIsActive()) {
            holder.chbUnit.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    public interface OnClick {
        void add(int position, int whichAdapter);
        void remove(int position, int whichAdapter);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUnit)
        TextView tvUnit;
        @BindView(R.id.tvUnitProperty)
        TextView tvUnitProperty;
        @BindView(R.id.chbUnit)
        MpCheckbox chbUnit;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(itemView).subscribe(aVoid -> {
                clickHandler();
            });

            RxView.clicks(chbUnit).subscribe(aVoid -> {
                clickHandler();
            });
        }

        private void clickHandler() {
            if (chbUnit.isCheckboxChecked()) {
                onClickCallback.remove(getAdapterPosition(), whichAdapter);
            } else {
                onClickCallback.add(getAdapterPosition(), whichAdapter);
            }
        }
    }
}
