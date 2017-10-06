package com.jim.multipos.ui.product.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.product.presenter.AdvancedOptionPresenter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 14.08.17.
 */

public class SubUnitsAdapter extends RecyclerView.Adapter<SubUnitsAdapter.ViewHolder> {
    private List<Unit> listOfUnits;
    private AdvancedOptionPresenter presenter;

    public SubUnitsAdapter(List<Unit> listOfUnits, AdvancedOptionPresenter presenter) {
        this.listOfUnits = listOfUnits;
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_units_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSubUnitName.setText(listOfUnits.get(position).getName());
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(0);
        DecimalFormat formatter = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        holder.tvMainUnitQty.setText(formatter.format(listOfUnits.get(position).getFactorRoot()) + " " + listOfUnits.get(position).getSubUnitAbbr());

    }

    @Override
    public int getItemCount() {
        return listOfUnits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSubUnitName)
        TextView tvSubUnitName;
        @BindView(R.id.tvMainUnitQty)
        TextView tvMainUnitQty;
        @BindView(R.id.ivRemove)
        ImageView ivRemove;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(ivRemove).subscribe(aVoid -> {
                presenter.removeSubUnit(getAdapterPosition());
            });
        }
    }
}
