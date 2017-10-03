package com.jim.multipos.ui.products.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Recipe;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.products.presenters.AdvancedOptionPresenter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 14.08.17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private List<Recipe>  recipeList;
    private AdvancedOptionPresenter presenter;

    public IngredientsAdapter(List<Recipe> recipeList, AdvancedOptionPresenter presenter) {
        this.recipeList = recipeList;
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSubUnitName.setText(recipeList.get(position).getIngredient().getName());
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(0);
        DecimalFormat formatter = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        holder.tvMainUnitQty.setText(formatter.format(recipeList.get(position).getFactorRoot()) + " " + recipeList.get(position).getIngredientUnit().getAbbr());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSubUnitName)
        TextView tvSubUnitName;
        @BindView(R.id.tvMainUnitQty)
        TextView tvMainUnitQty;
        @BindView(R.id.ivRemoveIngredient)
        ImageView ivRemoveIngredient;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(ivRemoveIngredient).subscribe(aVoid -> presenter.removeRecipe(getAdapterPosition()));
        }
    }
}
