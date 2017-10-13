package com.jim.multipos.ui.first_configure.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 12.10.17.
 */

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.ViewHolder> {
    public interface OnClickListener {
        void addUnitItem(Unit item);

        void removeUnitItem(Unit item);
    }

    private List<Unit> units;
    private OnClickListener onClickListener;

    public UnitAdapter(List<Unit> units, OnClickListener onClickListener) {
        this.units = units;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_item, parent, false);

        return new UnitAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        /*StringBuilder title = new StringBuilder();
        StringBuilder description = new StringBuilder();

        //Unit unit = units.get(position);
        //UnitCategory unitCategory = unit.getUnitCategory();

        title.append(units.get(position).getName());
        title.append(" (");
        title.append(units.get(position).getAbbr());
        title.append(")");

        description.append("1 ");
        description.append(units.get(position).getName());
        description.append(" = ");
        description.append(units.get(position).getFactorRoot());
        description.append(" ");
        description.append(units.get(position).getUnitCategory().getName());*/

        viewHolder.tvUnit.setText(String.format("%s (%s)", units.get(position).getName(), units.get(position).getAbbr()));
        viewHolder.tvUnitProperty.setText(String.format("1 %s = %f %s", units.get(position).getAbbr(),
                units.get(position).getFactorRoot(),
                units.get(position).getUnitCategory().getAbbr()));
        viewHolder.chbUnit.setChecked(units.get(position).getIsActive());
    }

    public void addUnitItem(Unit unit) {
        //units.add(unit);
        units.set(units.indexOf(unit), unit);
        notifyDataSetChanged();
    }

    public void removeUnitItem(Unit unit) {
        //units.remove(unit);
        units.set(units.indexOf(unit), unit);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return units.size();
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
                units.get(getAdapterPosition()).setIsActive(false);
                onClickListener.removeUnitItem(units.get(getAdapterPosition()));
            } else {
                units.get(getAdapterPosition()).setIsActive(true);
                onClickListener.addUnitItem(units.get(getAdapterPosition()));
            }
        }
    }
}
